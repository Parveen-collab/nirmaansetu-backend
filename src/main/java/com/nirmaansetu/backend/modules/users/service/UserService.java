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
}
