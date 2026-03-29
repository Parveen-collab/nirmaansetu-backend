package com.nirmaansetu.backend.modules.users.dto;

import com.nirmaansetu.backend.modules.users.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDto {
    @NotBlank(message = "Phone number is required")
    @Size(min = 7, max = 15, message = "Phone number must be between 7 and 15 characters")
    private String phoneNumber;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Aadhaar number is required")
    @Size(min = 12, max = 12, message = "Aadhaar number must be 12 digits")
    private String aadhaarNumber;

    @NotNull(message = "Role is required")
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

