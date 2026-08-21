package com.nirmaansetu.backend.modules.auth.mapper;

import com.nirmaansetu.backend.modules.auth.dto.JwtUserDto;
import com.nirmaansetu.backend.modules.users.entity.RegistrationStatus;
import com.nirmaansetu.backend.modules.users.entity.Role;
import com.nirmaansetu.backend.modules.users.entity.User;
import org.springframework.stereotype.Component;

@Component
public class JwtMapper {

    public JwtUserDto toJwtUser(User user) {
        RegistrationStatus status = user.getRegistrationStatus();
        if (status == null && user.getRole() == Role.GUEST) {
            status = RegistrationStatus.GUEST;
        } else if (status == null) {
            status = RegistrationStatus.ACTIVE;
        }

        return JwtUserDto.builder()
                .userId(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .registrationStatus(status)
                .build();
    }
}