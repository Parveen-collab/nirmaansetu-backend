package com.nirmaansetu.backend.modules.auth.service;

import com.nirmaansetu.backend.modules.auth.dto.AuthResponseDto;
import com.nirmaansetu.backend.modules.auth.dto.JwtUserDto;
import com.nirmaansetu.backend.modules.auth.dto.LoginRequestDto;
import com.nirmaansetu.backend.modules.auth.dto.VerifyOtpRequestDto;
import com.nirmaansetu.backend.modules.auth.mapper.JwtMapper;
import com.nirmaansetu.backend.modules.users.entity.User;
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
    private final JwtMapper jwtMapper;

    public AuthenticationService(
            OtpService otpService,
            UserService userService,
            JwtUtil jwtUtil,
            JwtMapper jwtMapper) {

        this.otpService = otpService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.jwtMapper = jwtMapper;
    }

    public AuthResponseDto login(LoginRequestDto request) {
        User user = userService.login(request);
        return buildAuthResponse(user);
    }

    public AuthResponseDto verifyOtp(VerifyOtpRequestDto request) {
        boolean verified = otpService.verifyOtp(request.getPhoneNumber(), request.getOtp());

        if (!verified) {
            throw new BadCredentialsException("Invalid or expired OTP");
        }

        User user = userService.createOrGetGuestUser(request.getPhoneNumber());
        return buildAuthResponse(user);
    }

    public AuthResponseDto refreshToken(String refreshToken) {
        String phone = jwtUtil.extractPhoneNumber(refreshToken);

        if (phone == null || !jwtUtil.validateToken(refreshToken, phone)) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        User user = userService.getUserEntityByPhoneNumber(phone);
        return buildAuthResponse(user);
    }

    public AuthResponseDto createAuthResponse(User user) {
        return buildAuthResponse(user);
    }

    private AuthResponseDto buildAuthResponse(User user) {
        JwtUserDto jwtUser = jwtMapper.toJwtUser(user);

        String accessToken = jwtUtil.generateToken(jwtUser, false);
        String refreshToken = jwtUtil.generateToken(jwtUser, true);

        return new AuthResponseDto(
                accessToken,
                refreshToken,
                jwtUser.getUserId(),
                jwtUser.getRole(),
                jwtUser.getRegistrationStatus()
        );
    }
}
