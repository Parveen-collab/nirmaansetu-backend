package com.nirmaansetu.backend.modules.users.dto;

public class UserRequestDto {
    private String phoneNumber;
    private String name;

    public UserRequestDto(String phoneNumber, String name, String email) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.email = email;
    }

    private String email;

    public String getPhoneNumber() {
    }

    public String getName() {
    }

    public String getEmail() {
    }
    // Getters and Setters
}

