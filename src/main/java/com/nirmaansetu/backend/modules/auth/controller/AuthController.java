//Expose endpoints for requesting and verifying the OTP.

package com.nirmaansetu.backend.modules.auth.controller;

import com.nirmaansetu.backend.modules.auth.dto.AuthResponseDto;
import com.nirmaansetu.backend.modules.auth.dto.OtpRequestDto;
import com.nirmaansetu.backend.modules.auth.dto.VerifyOtpRequestDto;
import com.nirmaansetu.backend.modules.auth.service.OtpService;
import com.nirmaansetu.backend.modules.auth.service.SmsService;
import com.nirmaansetu.backend.shared.utils.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth APIs", description = "Operations related to auth")
public class AuthController {
    @Autowired
    private OtpService otpService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@Valid @RequestBody OtpRequestDto request) {
        otpService.sendOtp(request.getPhoneNumber());
        return ResponseEntity.ok("OTP sent successfully");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtpRequestDto request) {
        if (otpService.verifyOtp(request.getPhoneNumber(), request.getOtp())) {
            String accessToken = jwtUtil.generateToken(request.getPhoneNumber(), false);
            String refreshToken = jwtUtil.generateToken(request.getPhoneNumber(), true);

            return ResponseEntity.ok(new AuthResponseDto(accessToken, refreshToken));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired OTP");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        String phoneNumber = jwtUtil.extractPhoneNumber(refreshToken);
        if (phoneNumber != null && jwtUtil.validateToken(refreshToken, phoneNumber)) {
            String newAccessToken = jwtUtil.generateToken(phoneNumber, false);
            String newRefreshToken = jwtUtil.generateToken(phoneNumber, true);
            return ResponseEntity.ok(new AuthResponseDto(newAccessToken, newRefreshToken));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }



    @RestController
    @RequestMapping("/api/sms")
    @Tag(name = "sms")
    public static class SmsController {

        @Autowired
        private SmsService smsService;

        @PostMapping("/send")
        public String sendSms(@RequestParam String phone) {

            smsService.sendSms(phone, "Welcome to NirmaanSetu!");

            return "SMS sent successfully";
        }
    }
}
