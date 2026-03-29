package com.nirmaansetu.backend.modules.users.dto;

import com.nirmaansetu.backend.modules.users.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long id;
    private String phoneNumber;
    private String name;
    private String email;
    private Role role;
    private String message;
}
