package com.nirmaansetu.backend.modules.users.dto;

import com.nirmaansetu.backend.modules.users.entity.AddressType;
import com.nirmaansetu.backend.modules.users.entity.Role;
import com.nirmaansetu.backend.modules.users.entity.ShopType;
import com.nirmaansetu.backend.modules.auth.globalNumberValidator.ValidPhoneNumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserRequestDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "Phone number is required")
    @ValidPhoneNumber
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
    public static class EmployeeProfileDto implements Serializable {
        private static final long serialVersionUID = 1L;
        @NotBlank(message = "Service category is required")
        private String serviceCategory;
        @NotBlank(message = "Service speciality is required")
        private String serviceSpeciality;
        @NotNull(message = "Experience years is required")
        @Min(value = 1, message = "Experience years must be greater than 0")
        private Integer experienceYears;
    }

    @Data
    public static class EmployerProfileDto implements Serializable {
        private static final long serialVersionUID = 1L;
        @NotBlank(message = "Company name is required")
        private String companyName;

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

    @Data
    public static class SupplierProfileDto implements Serializable {
        private static final long serialVersionUID = 1L;
        @NotBlank(message = "Shop name is required")
        private String shopName;
        @NotBlank(message = "Shop category is required")
        private String shopCategory;
        @NotBlank(message = "Shop speciality is required")
        private String shopSpeciality;
        @NotNull(message = "Shop type is required")
        private ShopType shopType;

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

    @Data
    public static class AddressDto implements Serializable {
        private static final long serialVersionUID = 1L;
        @NotNull(message = "Address type is required")
        private AddressType type; // PERMANENT | CURRENT
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

