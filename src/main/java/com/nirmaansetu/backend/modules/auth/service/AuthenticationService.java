package com.nirmaansetu.backend.modules.auth.service;

import com.nirmaansetu.backend.modules.auth.dto.AuthResponseDto;
import com.nirmaansetu.backend.modules.auth.dto.LoginRequestDto;
import com.nirmaansetu.backend.modules.auth.dto.VerifyOtpRequestDto;
import com.nirmaansetu.backend.modules.users.service.UserService;
import com.nirmaansetu.backend.shared.utils.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthenticationService {

    private final OtpService otpService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthenticationService(
            OtpService otpService,
            UserService userService,
            JwtUtil jwtUtil) {

        this.otpService = otpService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponseDto login(LoginRequestDto request) {

        userService.login(request);

        String accessToken =
                jwtUtil.generateToken(request.getPhoneNumber(), false);

        String refreshToken =
                jwtUtil.generateToken(request.getPhoneNumber(), true);

        return new AuthResponseDto(accessToken, refreshToken);
    }

    public AuthResponseDto verifyOtp(VerifyOtpRequestDto request) {

        boolean verified =
                otpService.verifyOtp(
                        request.getPhoneNumber(),
                        request.getOtp());

        if (!verified) {
            throw new BadCredentialsException(
                    "Invalid or expired OTP");
        }

        String accessToken =
                jwtUtil.generateToken(
                        request.getPhoneNumber(),
                        false);

        String refreshToken =
                jwtUtil.generateToken(
                        request.getPhoneNumber(),
                        true);

        return new AuthResponseDto(
                accessToken,
                refreshToken);
    }

    public AuthResponseDto refreshToken(String refreshToken) {

        String phone =
                jwtUtil.extractPhoneNumber(refreshToken);

        if (phone == null ||
                !jwtUtil.validateToken(refreshToken, phone)) {

            throw new BadCredentialsException(
                    "Invalid refresh token");
        }

        String access =
                jwtUtil.generateToken(phone, false);

        String refresh =
                jwtUtil.generateToken(phone, true);

        return new AuthResponseDto(
                access,
                refresh);
    }
}
