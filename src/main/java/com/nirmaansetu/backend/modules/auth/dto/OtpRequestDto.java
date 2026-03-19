package com.nirmaansetu.backend.modules.auth.dto;

import com.nirmaansetu.backend.modules.auth.globalNumberValidator.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpRequestDto {
    @NotBlank(message = "Phone number is required")
    @ValidPhoneNumber
    private String phoneNumber;
}

