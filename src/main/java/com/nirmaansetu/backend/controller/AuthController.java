//Expose endpoints for requesting and verifying the OTP.

package com.nirmaansetu.backend.controller;

import com.nirmaansetu.backend.dto.OtpRequestDto;
import com.nirmaansetu.backend.dto.VerifyOtpRequestDto;
import com.nirmaansetu.backend.service.OtpService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private OtpService otpService;

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@Valid @RequestBody OtpRequestDto request) {
        otpService.sendOtp(request.getPhoneNumber());
        return ResponseEntity.ok("OTP sent successfully");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@Valid @RequestBody VerifyOtpRequestDto request ) {
        if (otpService.verifyOtp(request.getPhoneNumber(), request.getOtp())) {
            return ResponseEntity.ok("Verification successful");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired OTP");
    }


}
