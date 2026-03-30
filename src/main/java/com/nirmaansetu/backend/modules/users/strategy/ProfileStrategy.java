package com.nirmaansetu.backend.modules.users.strategy;

import com.nirmaansetu.backend.modules.users.dto.UserRequestDto;
import com.nirmaansetu.backend.modules.users.entity.User;
import com.nirmaansetu.backend.modules.users.entity.Role;

public interface ProfileStrategy {
    Role getRole();
    void createProfile(User user, UserRequestDto request, String photoUrl);
    void updateProfile(User user, UserRequestDto request, String photoUrl);
}
