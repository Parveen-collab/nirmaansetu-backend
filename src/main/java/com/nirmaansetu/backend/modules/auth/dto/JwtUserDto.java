package com.nirmaansetu.backend.modules.auth.dto;

import com.nirmaansetu.backend.modules.users.entity.RegistrationStatus;
import com.nirmaansetu.backend.modules.users.entity.Role;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class JwtUserDto {

    private final Long userId;
    private final String phoneNumber;
    private final Role role;
    private final RegistrationStatus registrationStatus;
}
