package com.nirmaansetu.backend.modules.users.service;

import com.nirmaansetu.backend.modules.users.dto.UserRequestDto;
import com.nirmaansetu.backend.modules.users.dto.UserResponseDto;
import com.nirmaansetu.backend.modules.users.entity.*;
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

    @Transactional
    public UserResponseDto registerUser(UserRequestDto request) {
        User user = new User();
        user.setPhoneNumber(request.getPhoneNumber());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());

        User savedUser = userRepository.save(user);

        // Automate Profile Creation based on Role
        if (request.getRole() == Role.EMPLOYEE && request.getEmployeeProfile() != null) {
            EmployeeProfile employeeProfile = new EmployeeProfile();
            employeeProfile.setUser(savedUser);
            employeeProfile.setServiceCategory(request.getEmployeeProfile().getServiceCategory());
            employeeProfile.setServiceSpeciality(request.getEmployeeProfile().getServiceSpeciality());
            employeeProfile.setExperienceYears(request.getEmployeeProfile().getExperienceYears());
            employeeProfileRepository.save(employeeProfile);
        } else if (request.getRole() == Role.EMPLOYER && request.getEmployerProfile() != null) {
            EmployerProfile employerProfile = new EmployerProfile();
            employerProfile.setUser(savedUser);
            employerProfile.setCompanyName(request.getEmployerProfile().getCompanyName());
            employerProfile.setCompanyAddress(request.getEmployerProfile().getCompanyAddress());
            employerProfileRepository.save(employerProfile);
        } else if (request.getRole() == Role.SUPPLIER && request.getSupplierProfile() != null) {
            SupplierProfile supplierProfile = new SupplierProfile();
            supplierProfile.setUser(savedUser);
            supplierProfile.setShopName(request.getSupplierProfile().getShopName());
            supplierProfile.setShopCategory(request.getSupplierProfile().getShopCategory());
            supplierProfile.setShopSpeciality(request.getSupplierProfile().getShopSpeciality());
            supplierProfile.setShopType(ShopType.valueOf(request.getSupplierProfile().getShopType()));
            supplierProfile.setShopAddress(request.getSupplierProfile().getShopAddress());
            supplierProfileRepository.save(supplierProfile);
        }

        return new UserResponseDto(
                savedUser.getId(),
                savedUser.getPhoneNumber(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                "User registered successfully"
        );
    }
}
