package com.nirmaansetu.backend.dto;

import com.nirmaansetu.backend.globalNumberValidator.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpRequestDto {
    @NotBlank(message = "Phone number is required")
    @ValidPhoneNumber
    private String phoneNumber;
}

