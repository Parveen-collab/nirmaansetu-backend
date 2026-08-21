package com.nirmaansetu.backend.modules.auth.dto;

import com.nirmaansetu.backend.modules.users.entity.RegistrationStatus;
import com.nirmaansetu.backend.modules.users.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private Role role;
    private RegistrationStatus registrationStatus;
}
