package com.nirmaansetu.backend.modules.users.dto;

import com.nirmaansetu.backend.modules.auth.dto.AuthResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationResponseDto {
    private UserResponseDto user;
    private AuthResponseDto auth;
}
