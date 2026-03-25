//Expose endpoints for requesting and verifying the OTP.

package com.nirmaansetu.backend.modules.auth.controller;

import com.nirmaansetu.backend.modules.auth.dto.AuthResponseDto;
import com.nirmaansetu.backend.modules.auth.dto.OtpRequestDto;
import com.nirmaansetu.backend.modules.auth.dto.VerifyOtpRequestDto;
import com.nirmaansetu.backend.modules.auth.service.OtpService;
import com.nirmaansetu.backend.modules.auth.service.SmsService;
import com.nirmaansetu.backend.shared.utils.JwtUtil;
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

//    @PostMapping("/verify-otp")
//    public ResponseEntity<String> verifyOtp(@Valid @RequestBody VerifyOtpRequestDto request ) {
//        if (otpService.verifyOtp(request.getPhoneNumber(), request.getOtp())) {
//            return ResponseEntity.ok("Verification successful");
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired OTP");
//    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtpRequestDto request) {
        if (otpService.verifyOtp(request.getPhoneNumber(), request.getOtp())) {
            JwtUtil jwtUtil = new JwtUtil();
            String accessToken = jwtUtil.generateToken(request.getPhoneNumber(), false);
            String refreshToken = jwtUtil.generateToken(request.getPhoneNumber(), true);

            return ResponseEntity.ok(new AuthResponseDto(accessToken, refreshToken));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired OTP");
    }



    @RestController
    @RequestMapping("/api/sms")
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
