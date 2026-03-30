package com.nirmaansetu.backend.modules.users.service;

import com.nirmaansetu.backend.modules.users.dto.UserRequestDto;
import com.nirmaansetu.backend.modules.users.dto.UserResponseDto;
import com.nirmaansetu.backend.modules.users.entity.*;
import com.nirmaansetu.backend.modules.users.mapper.UserMapper;
import com.nirmaansetu.backend.modules.users.repository.UserRepository;
import com.nirmaansetu.backend.modules.users.strategy.ProfileStrategyFactory;
import com.nirmaansetu.backend.shared.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileStrategyFactory profileStrategyFactory;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public UserResponseDto registerUser(UserRequestDto request, MultipartFile photo) {
        User user = userMapper.toUser(request);

        // Ensure bidirectional relationship for addresses
        if (user.getAddresses() != null) {
            user.getAddresses().forEach(address -> address.setUser(user));
        }

        User savedUser = userRepository.save(user);

        String photoUrl = fileService.saveProfilePhoto(photo);

        // Automate Profile Creation based on Role using Strategy Pattern
        profileStrategyFactory.getStrategy(request.getRole())
                .createProfile(savedUser, request, photoUrl);

        return userMapper.toUserResponseDto(savedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto request, MultipartFile photo) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUserFromDto(request, user);

        String photoUrl = fileService.saveProfilePhoto(photo);

        // Handle profile updates based on role using Strategy Pattern
        profileStrategyFactory.getStrategy(user.getRole())
                .updateProfile(user, request, photoUrl);

        User updatedUser = userRepository.save(user);
        UserResponseDto response = userMapper.toUserResponseDto(updatedUser);
        response.setMessage("User updated successfully");
        return response;
    }
}
