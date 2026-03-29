package com.nirmaansetu.backend.modules.users.dto;

import com.nirmaansetu.backend.modules.users.entity.Role;
import lombok.Data;

@Data
public class UserRequestDto {
    private String phoneNumber;
    private String name;
    private String email;
    private Role role;

    // Profile specific data (could be separate DTOs)
    private EmployeeProfileDto employeeProfile;
    private EmployerProfileDto employerProfile;
    private SupplierProfileDto supplierProfile;

    @Data
    public static class EmployeeProfileDto {
        private String serviceCategory;
        private String serviceSpeciality;
        private int experienceYears;
    }

    @Data
    public static class EmployerProfileDto {
        private String companyName;
        private String companyAddress;
    }

    @Data
    public static class SupplierProfileDto {
        private String shopName;
        private String shopCategory;
        private String shopSpeciality;
        private String shopType;
        private String shopAddress;
    }
}

