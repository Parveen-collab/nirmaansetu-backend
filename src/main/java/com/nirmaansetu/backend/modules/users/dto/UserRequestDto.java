package com.nirmaansetu.backend.modules.users.dto;

public class UserRequestDto {
    private String phoneNumber;
    private String name;
    private String email;

    public UserRequestDto(String phoneNumber, String name, String email) {
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    // Getters and Setters
}

