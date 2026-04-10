package com.nirmaansetu.backend.modules.users.dto;

import com.nirmaansetu.backend.modules.users.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String phoneNumber;
    private String name;
    private String email;
    private String username;
    private String password;
    private String aadhaarNumber;
    private Role role;
    private String profileImageUrl;
    private List<UserRequestDto.AddressDto> addresses;
    private UserRequestDto.EmployeeProfileDto employeeProfile;
    private UserRequestDto.EmployerProfileDto employerProfile;
    private UserRequestDto.SupplierProfileDto supplierProfile;
    private String message;
}
