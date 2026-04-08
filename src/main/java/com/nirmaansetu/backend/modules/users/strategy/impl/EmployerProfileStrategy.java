package com.nirmaansetu.backend.modules.users.strategy.impl;

import com.nirmaansetu.backend.modules.users.dto.UserRequestDto;
import com.nirmaansetu.backend.modules.users.entity.EmployerProfile;
import com.nirmaansetu.backend.modules.users.entity.Role;
import com.nirmaansetu.backend.modules.users.entity.User;
import com.nirmaansetu.backend.modules.users.mapper.UserMapper;
import com.nirmaansetu.backend.modules.users.repository.EmployerProfileRepository;
import com.nirmaansetu.backend.modules.users.strategy.ProfileStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployerProfileStrategy implements ProfileStrategy {

    @Autowired
    private EmployerProfileRepository employerProfileRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Role getRole() {
        return Role.EMPLOYER;
    }

    @Override
    public void createProfile(User user, UserRequestDto request, String photoUrl) {
        if (request.getEmployerProfile() != null) {
            EmployerProfile profile = userMapper.toEmployerProfile(request.getEmployerProfile());
            profile.setUser(user);
            profile.setPhotoUrl(photoUrl);
            employerProfileRepository.save(profile);
            user.setEmployerProfile(profile);
        }
    }

    @Override
    public void updateProfile(User user, UserRequestDto request, String photoUrl) {
        EmployerProfile profile = user.getEmployerProfile();
        if (request.getEmployerProfile() != null) {
            if (profile == null) {
                profile = userMapper.toEmployerProfile(request.getEmployerProfile());
                profile.setUser(user);
                user.setEmployerProfile(profile);
            } else {
                userMapper.updateEmployerProfileFromDto(request.getEmployerProfile(), profile);
            }
        }
        if (photoUrl != null) {
            if (profile == null) {
                profile = new EmployerProfile();
                profile.setUser(user);
                user.setEmployerProfile(profile);
            }
            profile.setPhotoUrl(photoUrl);
        }
        if (profile != null) {
            employerProfileRepository.save(profile);
        }
    }
}
