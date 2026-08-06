package com.nirmaansetu.backend.modules.auth.mapper;

import com.nirmaansetu.backend.modules.auth.dto.JwtUserDto;
import com.nirmaansetu.backend.modules.users.entity.User;
import org.springframework.stereotype.Component;

@Component
public class JwtMapper {

    public JwtUserDto toJwtUser(User user) {

        return JwtUserDto.builder()
                .userId(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .registrationStatus(user.getRegistrationStatus())
                .build();
    }
}