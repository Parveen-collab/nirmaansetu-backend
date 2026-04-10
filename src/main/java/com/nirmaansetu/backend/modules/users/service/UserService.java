package com.nirmaansetu.backend.modules.users.service;

import com.nirmaansetu.backend.modules.users.dto.UserRequestDto;
import com.nirmaansetu.backend.modules.users.dto.UserResponseDto;
import com.nirmaansetu.backend.modules.auth.service.OtpService;
import com.nirmaansetu.backend.modules.auth.service.SmsService;
import com.nirmaansetu.backend.modules.users.entity.*;
import com.nirmaansetu.backend.modules.users.mapper.UserMapper;
import com.nirmaansetu.backend.modules.users.repository.UserRepository;
import com.nirmaansetu.backend.modules.users.strategy.ProfileStrategyFactory;
import com.nirmaansetu.backend.shared.service.FileService;
import com.nirmaansetu.backend.shared.service.PhotoHashService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileStrategyFactory profileStrategyFactory;

    @Autowired
    private FileService fileService;

    @Autowired
    private PhotoHashService photoHashService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OtpService otpService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto registerUser(@Valid UserRequestDto request, MultipartFile photo) {
        validateRequest(request);
        if (!otpService.isPhoneNumberVerified(request.getPhoneNumber())) {
            throw new RuntimeException("Phone number not verified via OTP");
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new RuntimeException("Phone number already exists");
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty() && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (request.getAadhaarNumber() != null && !request.getAadhaarNumber().isEmpty() && userRepository.existsByAadhaarNumber(request.getAadhaarNumber())) {
            throw new RuntimeException("Aadhaar number already exists");
        }

        User user = userMapper.toUser(request);
        
        // Generate a random password
        String plainPassword = java.util.UUID.randomUUID().toString().substring(0, 8);
        user.setPassword(passwordEncoder.encode(plainPassword));

        // Calculate and check photo hash for uniqueness
        String photoHash = null;
        if (photo != null && !photo.isEmpty()) {
            photoHash = photoHashService.calculateHash(photo);
        } else if (request.getProfileImageUrl() != null && !request.getProfileImageUrl().isEmpty()) {
            photoHash = photoHashService.calculateHashFromUrl(request.getProfileImageUrl());
        }

        if (photoHash != null && userRepository.existsByPhotoHash(photoHash)) {
            throw new RuntimeException("Profile photo already exists");
        }

        // Ensure bidirectional relationship for addresses
        if (user.getAddresses() != null) {
            user.getAddresses().forEach(address -> address.setUser(user));
        }

        User savedUser = userRepository.save(user);

        String photoUrl = fileService.saveProfilePhoto(photo);

        if (photoUrl == null) {
            photoUrl = request.getProfileImageUrl();
        }

        savedUser.setProfileImageUrl(photoUrl);
        savedUser.setPhotoHash(photoHash);
        userRepository.save(savedUser);

        // Automate Profile Creation based on Role using Strategy Pattern
        profileStrategyFactory.getStrategy(request.getRole())
                .createProfile(savedUser, request, photoUrl);

        // Clear verification status after successful registration
        otpService.clearVerification(request.getPhoneNumber());

        // Send SMS with username and password
        String messageBody = String.format("Welcome to NirmaanSetu! Your username is %s and your password is %s. Please login to continue.", 
            savedUser.getPhoneNumber(), plainPassword);
        smsService.sendSms(savedUser.getPhoneNumber(), messageBody);

        UserResponseDto response = userMapper.toUserResponseDto(savedUser);
        response.setPassword(plainPassword);
        return response;
    }

    private void validateRequest(UserRequestDto request) {
        if (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty()) {
            throw new RuntimeException("Null/Empty Fields : Phone number is required");
        }
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new RuntimeException("Null/Empty Fields : Name is required");
        }
        if (request.getAadhaarNumber() == null || request.getAadhaarNumber().trim().isEmpty()) {
            throw new RuntimeException("Null/Empty Fields : Aadhaar number is required");
        }
        if (request.getRole() == null) {
            throw new RuntimeException("Null/Empty Fields : Role is required");
        }

        // 1. Inconsistent Profile
        validateProfileConsistency(request);

        // 2. Address Limits
        validateAddresses(request);

        // 3. Image Upload Failures (profileImageUrl in request)
        validateProfileImageUrl(request.getProfileImageUrl());
    }

    private void validateProfileConsistency(UserRequestDto request) {
        Role role = request.getRole();
        if (role == Role.EMPLOYEE) {
            if (request.getEmployeeProfile() == null) {
                throw new RuntimeException("Null/Empty Fields : Employee profile data is required for role EMPLOYEE");
            }
            if (request.getEmployerProfile() != null || request.getSupplierProfile() != null) {
                throw new RuntimeException("Inconsistent Profile : Providing employer or supplier profile data while role is EMPLOYEE");
            }
        } else if (role == Role.EMPLOYER) {
            if (request.getEmployerProfile() == null) {
                throw new RuntimeException("Null/Empty Fields : Employer profile data is required for role EMPLOYER");
            }
            if (request.getEmployeeProfile() != null || request.getSupplierProfile() != null) {
                throw new RuntimeException("Inconsistent Profile : Providing employee or supplier profile data while role is EMPLOYER");
            }
        } else if (role == Role.SUPPLIER) {
            if (request.getSupplierProfile() == null) {
                throw new RuntimeException("Null/Empty Fields : Supplier profile data is required for role SUPPLIER");
            }
            if (request.getEmployeeProfile() != null || request.getEmployerProfile() != null) {
                throw new RuntimeException("Inconsistent Profile : Providing employee or employer profile data while role is SUPPLIER");
            }
        }
    }

    private void validateAddresses(UserRequestDto request) {
        if (request.getAddresses() == null || request.getAddresses().isEmpty()) {
            throw new RuntimeException("Address Limits : Addresses are required");
        }

        Set<String> types = new HashSet<>();
        boolean currentExists = false;
        boolean permanentExists = false;

        for (UserRequestDto.AddressDto address : request.getAddresses()) {
            if (address.getType() == null) {
                throw new RuntimeException("Address Limits : Address type is required");
            }

            String type = address.getType().name();
            if (types.contains(type)) {
                throw new RuntimeException("Address Limits : Multiple " + type + " addresses are not allowed");
            }
            types.add(type);

            if (AddressType.CURRENT.equals(address.getType())) currentExists = true;
            if (AddressType.PERMANENT.equals(address.getType())) permanentExists = true;
        }

        if (!currentExists || !permanentExists) {
            throw new RuntimeException("Address Limits : Both CURRENT and PERMANENT addresses are required");
        }
    }

    private void validateProfileImageUrl(String profileImageUrl) {
        if (profileImageUrl == null || profileImageUrl.isEmpty()) {
            return;
        }

        try {
            URI uri = new URI(profileImageUrl);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int responseCode = connection.getResponseCode();
            if (responseCode < 200 || responseCode >= 400) {
                throw new RuntimeException("Image Upload Failures : Profile image URL is inaccessible");
            }
        } catch (Exception e) {
            throw new RuntimeException("Image Upload Failures : Profile image URL is malformed or inaccessible: " + e.getMessage());
        }
    }

    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Cacheable(value = "users", key = "#id")
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserResponseDto(user);
    }

    public List<UserResponseDto> getAllUsersExceptSuperAdmin(Role role) {
        List<User> users;
        if (role != null) {
            users = userRepository.findByRole(role);
        } else {
            users = userRepository.findByRoleNot(Role.SUPER_ADMIN);
        }
        return users.stream()
                .map(userMapper::toUserResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public UserResponseDto updateUser(Long id, @Valid UserRequestDto request, MultipartFile photo) {
        validateRequest(request);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUserFromDto(request, user);

        // Calculate and check photo hash for uniqueness
        String photoHash = null;
        if (photo != null && !photo.isEmpty()) {
            photoHash = photoHashService.calculateHash(photo);
        } else if (request.getProfileImageUrl() != null && !request.getProfileImageUrl().isEmpty()) {
            photoHash = photoHashService.calculateHashFromUrl(request.getProfileImageUrl());
        }

        if (photoHash != null && userRepository.existsByPhotoHashAndIdNot(photoHash, id)) {
            throw new RuntimeException("Profile photo already exists");
        }

        String photoUrl = fileService.saveProfilePhoto(photo);

        if (photoUrl == null && request.getProfileImageUrl() != null) {
            photoUrl = request.getProfileImageUrl();
        }

        if (photoUrl != null) {
            user.setProfileImageUrl(photoUrl);
            user.setPhotoHash(photoHash);
        }

        // Handle profile updates based on role using Strategy Pattern
        profileStrategyFactory.getStrategy(user.getRole())
                .updateProfile(user, request, photoUrl);

        User updatedUser = userRepository.save(user);
        UserResponseDto response = userMapper.toUserResponseDto(updatedUser);
        response.setMessage("User updated successfully");
        return response;
    }
}
