package com.nirmaansetu.backend.modules.users.strategy.impl;

import com.nirmaansetu.backend.modules.users.dto.UserRequestDto;
import com.nirmaansetu.backend.modules.users.entity.EmployeeProfile;
import com.nirmaansetu.backend.modules.users.entity.Role;
import com.nirmaansetu.backend.modules.users.entity.User;
import com.nirmaansetu.backend.modules.users.mapper.UserMapper;
import com.nirmaansetu.backend.modules.users.repository.EmployeeProfileRepository;
import com.nirmaansetu.backend.modules.users.strategy.ProfileStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeProfileStrategy implements ProfileStrategy {

    @Autowired
    private EmployeeProfileRepository employeeProfileRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Role getRole() {
        return Role.EMPLOYEE;
    }

    @Override
    public void createProfile(User user, UserRequestDto request, String photoUrl) {
        if (request.getEmployeeProfile() != null) {
            EmployeeProfile profile = userMapper.toEmployeeProfile(request.getEmployeeProfile());
            profile.setUser(user);
            profile.setPhotoUrl(photoUrl);
            employeeProfileRepository.save(profile);
            user.setEmployeeProfile(profile);
        }
    }

    @Override
    public void updateProfile(User user, UserRequestDto request, String photoUrl) {
        EmployeeProfile profile = user.getEmployeeProfile();
        if (request.getEmployeeProfile() != null) {
            if (profile == null) {
                profile = userMapper.toEmployeeProfile(request.getEmployeeProfile());
                profile.setUser(user);
                user.setEmployeeProfile(profile);
            } else {
                if (request.getEmployeeProfile().getServiceCategory() != null)
                    profile.setServiceCategory(request.getEmployeeProfile().getServiceCategory());
                if (request.getEmployeeProfile().getServiceSpeciality() != null)
                    profile.setServiceSpeciality(request.getEmployeeProfile().getServiceSpeciality());
                if (request.getEmployeeProfile().getExperienceYears() != 0)
                    profile.setExperienceYears(request.getEmployeeProfile().getExperienceYears());
            }
        }
        if (photoUrl != null) {
            if (profile == null) {
                profile = new EmployeeProfile();
                profile.setUser(user);
                user.setEmployeeProfile(profile);
            }
            profile.setPhotoUrl(photoUrl);
        }
        if (profile != null) {
            employeeProfileRepository.save(profile);
        }
    }
}
