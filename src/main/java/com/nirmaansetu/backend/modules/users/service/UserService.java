package com.nirmaansetu.backend.modules.users.service;

import com.nirmaansetu.backend.modules.users.dto.UserRequestDto;
import com.nirmaansetu.backend.modules.users.dto.UserResponseDto;
import com.nirmaansetu.backend.modules.users.entity.User;
import com.nirmaansetu.backend.modules.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserResponseDto registerUser(UserRequestDto request) {
        User user = new User();
        user.setPhoneNumber(request.getPhoneNumber());
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        User savedUser = userRepository.save(user);
        return new UserResponseDto(savedUser.getId(), savedUser.getPhoneNumber(), savedUser.getName());
    }
}
