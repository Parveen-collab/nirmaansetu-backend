package com.nirmaansetu.backend.modules.users.service;

import com.nirmaansetu.backend.modules.users.dto.UserRequestDto;
import com.nirmaansetu.backend.modules.users.dto.UserResponseDto;
import com.nirmaansetu.backend.modules.users.entity.*;
import com.nirmaansetu.backend.modules.users.mapper.UserMapper;
import com.nirmaansetu.backend.modules.users.repository.EmployeeProfileRepository;
import com.nirmaansetu.backend.modules.users.repository.EmployerProfileRepository;
import com.nirmaansetu.backend.modules.users.repository.SupplierProfileRepository;
import com.nirmaansetu.backend.modules.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeProfileRepository employeeProfileRepository;

    @Autowired
    private EmployerProfileRepository employerProfileRepository;

    @Autowired
    private SupplierProfileRepository supplierProfileRepository;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public UserResponseDto registerUser(UserRequestDto request) {
        User user = userMapper.toUser(request);

        // Ensure bidirectional relationship for addresses
        if (user.getAddresses() != null) {
            user.getAddresses().forEach(address -> address.setUser(user));
        }

        User savedUser = userRepository.save(user);

        // Automate Profile Creation based on Role
        if (request.getRole() == Role.EMPLOYEE && request.getEmployeeProfile() != null) {
            EmployeeProfile employeeProfile = userMapper.toEmployeeProfile(request.getEmployeeProfile());
            employeeProfile.setUser(savedUser);
            employeeProfileRepository.save(employeeProfile);
        } else if (request.getRole() == Role.EMPLOYER && request.getEmployerProfile() != null) {
            EmployerProfile employerProfile = userMapper.toEmployerProfile(request.getEmployerProfile());
            employerProfile.setUser(savedUser);
            employerProfileRepository.save(employerProfile);
        } else if (request.getRole() == Role.SUPPLIER && request.getSupplierProfile() != null) {
            SupplierProfile supplierProfile = userMapper.toSupplierProfile(request.getSupplierProfile());
            supplierProfile.setUser(savedUser);
            supplierProfileRepository.save(supplierProfile);
        }

        return userMapper.toUserResponseDto(savedUser);
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
    public UserResponseDto updateUser(Long id, UserRequestDto request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUserFromDto(request, user);

        // Handle profile updates based on role
        if (user.getRole() == Role.EMPLOYEE && request.getEmployeeProfile() != null) {
            if (user.getEmployeeProfile() == null) {
                EmployeeProfile profile = userMapper.toEmployeeProfile(request.getEmployeeProfile());
                profile.setUser(user);
                user.setEmployeeProfile(profile);
            } else {
                // Assuming we want to update existing profile fields
                EmployeeProfile profile = user.getEmployeeProfile();
                if (request.getEmployeeProfile().getServiceCategory() != null)
                    profile.setServiceCategory(request.getEmployeeProfile().getServiceCategory());
                if (request.getEmployeeProfile().getServiceSpeciality() != null)
                    profile.setServiceSpeciality(request.getEmployeeProfile().getServiceSpeciality());
                if (request.getEmployeeProfile().getExperienceYears() != 0)
                    profile.setExperienceYears(request.getEmployeeProfile().getExperienceYears());
            }
        } else if (user.getRole() == Role.EMPLOYER && request.getEmployerProfile() != null) {
            if (user.getEmployerProfile() == null) {
                EmployerProfile profile = userMapper.toEmployerProfile(request.getEmployerProfile());
                profile.setUser(user);
                user.setEmployerProfile(profile);
            } else {
                EmployerProfile profile = user.getEmployerProfile();
                if (request.getEmployerProfile().getCompanyName() != null)
                    profile.setCompanyName(request.getEmployerProfile().getCompanyName());
                if (request.getEmployerProfile().getCompanyAddress() != null)
                    profile.setCompanyAddress(request.getEmployerProfile().getCompanyAddress());
            }
        } else if (user.getRole() == Role.SUPPLIER && request.getSupplierProfile() != null) {
            if (user.getSupplierProfile() == null) {
                SupplierProfile profile = userMapper.toSupplierProfile(request.getSupplierProfile());
                profile.setUser(user);
                user.setSupplierProfile(profile);
            } else {
                SupplierProfile profile = user.getSupplierProfile();
                if (request.getSupplierProfile().getShopName() != null)
                    profile.setShopName(request.getSupplierProfile().getShopName());
                if (request.getSupplierProfile().getShopCategory() != null)
                    profile.setShopCategory(request.getSupplierProfile().getShopCategory());
                if (request.getSupplierProfile().getShopSpeciality() != null)
                    profile.setShopSpeciality(request.getSupplierProfile().getShopSpeciality());
                if (request.getSupplierProfile().getShopAddress() != null)
                    profile.setShopAddress(request.getSupplierProfile().getShopAddress());
                if (request.getSupplierProfile().getShopType() != null)
                    profile.setShopType(userMapper.stringToShopType(request.getSupplierProfile().getShopType()));
            }
        }

        User updatedUser = userRepository.save(user);
        UserResponseDto response = userMapper.toUserResponseDto(updatedUser);
        response.setMessage("User updated successfully");
        return response;
    }
}
