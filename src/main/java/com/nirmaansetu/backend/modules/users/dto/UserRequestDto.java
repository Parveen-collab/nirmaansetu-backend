package com.nirmaansetu.backend.modules.users.dto;

import com.nirmaansetu.backend.modules.users.entity.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

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

    private String profileImageUrl;

    @Valid
    private List<AddressDto> addresses;

    // Profile specific data (could be separate DTOs)
    @Valid
    private EmployeeProfileDto employeeProfile;
    @Valid
    private EmployerProfileDto employerProfile;
    @Valid
    private SupplierProfileDto supplierProfile;

    @Data
    public static class EmployeeProfileDto {
        @NotBlank(message = "Service category is required")
        private String serviceCategory;
        @NotBlank(message = "Service speciality is required")
        private String serviceSpeciality;
        @NotNull(message = "Experience years is required")
        private int experienceYears;
    }

    @Data
    public static class EmployerProfileDto {
        @NotBlank(message = "Company name is required")
        private String companyName;
        @NotBlank(message = "Company address is required")
        private String companyAddress;
    }

    @Data
    public static class SupplierProfileDto {
        @NotBlank(message = "Shop name is required")
        private String shopName;
        @NotBlank(message = "Shop category is required")
        private String shopCategory;
        @NotBlank(message = "Shop speciality is required")
        private String shopSpeciality;
        @NotBlank(message = "Shop type is required")
        private String shopType;
        @NotBlank(message = "Shop address is required")
        private String shopAddress;
    }

    @Data
    public static class AddressDto {
        @NotBlank(message = "Address type is required")
        private String type; // PERMANENT | CURRENT
        @NotBlank(message = "State is required")
        private String state;
        @NotBlank(message = "District is required")
        private String district;
        private String wardNumber;
        private String landmark;
        @NotBlank(message = "Pincode is required")
        private String pincode;
        @NotBlank(message = "Area/Village is required")
        private String areaVillage;
        private String building;
    }
}

