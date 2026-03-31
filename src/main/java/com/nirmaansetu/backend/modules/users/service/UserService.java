package com.nirmaansetu.backend.modules.users.service;

import com.nirmaansetu.backend.modules.users.dto.UserRequestDto;
import com.nirmaansetu.backend.modules.users.dto.UserResponseDto;
import com.nirmaansetu.backend.modules.users.entity.*;
import com.nirmaansetu.backend.modules.users.mapper.UserMapper;
import com.nirmaansetu.backend.modules.users.repository.UserRepository;
import com.nirmaansetu.backend.modules.users.strategy.ProfileStrategyFactory;
import com.nirmaansetu.backend.shared.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileStrategyFactory profileStrategyFactory;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public UserResponseDto registerUser(UserRequestDto request, MultipartFile photo) {
        validateRequest(request);
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

        // Ensure bidirectional relationship for addresses
        if (user.getAddresses() != null) {
            user.getAddresses().forEach(address -> address.setUser(user));
        }

        User savedUser = userRepository.save(user);

        String photoUrl = fileService.saveProfilePhoto(photo);

        if (photoUrl == null) {
            photoUrl = request.getProfileImageUrl();
        }

        if (photoUrl != null && userRepository.existsByProfileImageUrl(photoUrl)) {
            throw new RuntimeException("Profile photo already exists");
        }

        savedUser.setProfileImageUrl(photoUrl);
        userRepository.save(savedUser);

        // Automate Profile Creation based on Role using Strategy Pattern
        profileStrategyFactory.getStrategy(request.getRole())
                .createProfile(savedUser, request, photoUrl);

        return userMapper.toUserResponseDto(savedUser);
    }

    private void validateRequest(UserRequestDto request) {
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
            if (request.getEmployerProfile() != null || request.getSupplierProfile() != null) {
                throw new RuntimeException("Inconsistent Profile : Providing employer or supplier profile data while role is EMPLOYEE");
            }
        } else if (role == Role.EMPLOYER) {
            if (request.getEmployeeProfile() != null || request.getSupplierProfile() != null) {
                throw new RuntimeException("Inconsistent Profile : Providing employee or supplier profile data while role is EMPLOYER");
            }
        } else if (role == Role.SUPPLIER) {
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

            String type = address.getType().toUpperCase();
            if (types.contains(type)) {
                throw new RuntimeException("Address Limits : Multiple " + type + " addresses are not allowed");
            }
            types.add(type);

            if ("CURRENT".equals(type)) currentExists = true;
            if ("PERMANENT".equals(type)) permanentExists = true;
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
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto request, MultipartFile photo) {
        validateRequest(request);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUserFromDto(request, user);

        String photoUrl = fileService.saveProfilePhoto(photo);

        if (photoUrl == null && request.getProfileImageUrl() != null) {
            photoUrl = request.getProfileImageUrl();
        }

        if (photoUrl != null) {
            user.setProfileImageUrl(photoUrl);
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
