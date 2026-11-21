package com.nirmaansetu.backend.modules.auth.dto;

import com.nirmaansetu.backend.modules.users.entity.RegistrationStatus;
import com.nirmaansetu.backend.modules.users.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private Role role;
    private RegistrationStatus registrationStatus;
    private String phoneNumber;
    private String name;

    public AuthResponseDto(String accessToken, String refreshToken, Long userId, Role role, RegistrationStatus registrationStatus) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.role = role;
        this.registrationStatus = registrationStatus;
    }
}
