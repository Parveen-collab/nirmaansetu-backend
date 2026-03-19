package com.nirmaansetu.backend.modules.auth.dto;

import com.nirmaansetu.backend.modules.auth.globalNumberValidator.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class VerifyOtpRequestDto {
    @NotBlank(message = "Phone number is required")
    @ValidPhoneNumber
    private String phoneNumber;

    @NotBlank(message = "OTP is required")
    @Pattern(regexp = "^[0-9]{4}$", message = "OTP must be exactly 4 digits") // Adjust length if needed
    private String otp;
}

