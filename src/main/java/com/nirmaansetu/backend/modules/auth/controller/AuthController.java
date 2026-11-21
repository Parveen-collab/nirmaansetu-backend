package com.nirmaansetu.backend.modules.auth.controller;

import com.nirmaansetu.backend.modules.auth.dto.AuthResponseDto;
import com.nirmaansetu.backend.modules.auth.dto.LoginRequestDto;
import com.nirmaansetu.backend.modules.auth.dto.OtpRequestDto;
import com.nirmaansetu.backend.modules.auth.dto.RefreshTokenRequestDto;
import com.nirmaansetu.backend.modules.auth.dto.ResetPasswordRequestDto;
import com.nirmaansetu.backend.modules.auth.dto.VerifyOtpRequestDto;
import com.nirmaansetu.backend.modules.auth.service.AuthenticationService;
import com.nirmaansetu.backend.modules.auth.service.OtpService;
import com.nirmaansetu.backend.modules.auth.service.SmsService;
import com.nirmaansetu.backend.modules.users.entity.User;
import com.nirmaansetu.backend.modules.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth APIs", description = "Operations related to authentication")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserService userService;

    /**
     * Login using phone number and password.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody LoginRequestDto request) {

        return ResponseEntity.ok(authenticationService.login(request));
    }

    /**
     * Send OTP for registration/login.
     */
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(
            @Valid @RequestBody OtpRequestDto request) {

        otpService.sendOtp(request.getPhoneNumber());

        return ResponseEntity.ok("OTP sent successfully");
    }

    /**
     * Verify OTP, create a guest user (or return existing user), and issue JWT tokens.
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponseDto> verifyOtp(
            @Valid @RequestBody VerifyOtpRequestDto request) {

        return ResponseEntity.ok(authenticationService.verifyOtp(request));
    }

    /**
     * Send OTP for forgot password.
     */
    @PostMapping("/send-otp-forgot")
    public ResponseEntity<String> sendOtpForgot(
            @Valid @RequestBody OtpRequestDto request) {

        if (!userService.existsByPhoneNumber(request.getPhoneNumber())) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("User not found with this phone number");
        }

        otpService.sendOtp(request.getPhoneNumber());

        return ResponseEntity.ok("OTP sent successfully for password reset");
    }

    /**
     * Reset password after OTP verification.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordRequestDto request) {

        userService.resetPassword(request);

        return ResponseEntity.ok("Password reset successfully");
    }

    /**
     * Refresh access token using refresh token.
     * Case 1: If requested within the valid refresh window, returns new access & refresh tokens.
     * Case 2: If requested after the refresh window (or revoked), returns 401 Unauthorized so client redirects to login.
     */
    @Operation(
            summary = "Refresh Access Token",
            description = "Generate a new access token using a valid refresh token. Supported via query param or JSON body."
    )
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refreshToken(
            @RequestParam(required = false) String refreshToken,
            @RequestBody(required = false) RefreshTokenRequestDto request) {

        String token = (request != null && request.getRefreshToken() != null && !request.getRefreshToken().isBlank())
                ? request.getRefreshToken()
                : refreshToken;

        if (token == null || token.isBlank()) {
            throw new BadCredentialsException("Refresh token is required");
        }

        return ResponseEntity.ok(authenticationService.refreshToken(token));
    }

    /**
     * Validates current active session (within access token time window).
     * Used by existing user visiting the site to directly redirect to dashboard (Case 1).
     */
    @Operation(
            summary = "Get current authenticated session",
            description = "Validates the active session and returns user identity, role, and registration status.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/me")
    public ResponseEntity<AuthResponseDto> getCurrentSession() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof User user)) {
            throw new BadCredentialsException("Not authenticated");
        }
        return ResponseEntity.ok(authenticationService.getCurrentSession(user));
    }

    /**
     * Logout and revoke the active refresh token.
     */
    @Operation(
            summary = "Logout user",
            description = "Revokes the active refresh token.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestParam(required = false) String refreshToken,
            @RequestBody(required = false) RefreshTokenRequestDto request) {

        String token = (request != null && request.getRefreshToken() != null && !request.getRefreshToken().isBlank())
                ? request.getRefreshToken()
                : refreshToken;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (auth != null && auth.getPrincipal() instanceof User u) ? u : null;

        authenticationService.logout(token, user);
        return ResponseEntity.ok("Logged out successfully");
    }

    /**
     * SMS APIs
     */
    @RestController
    @RequestMapping("/api/v1/sms")
    @Tag(name = "SMS APIs")
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
