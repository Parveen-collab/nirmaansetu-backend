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
    private final RefreshTokenService refreshTokenService;

    public AuthenticationService(
            OtpService otpService,
            UserService userService,
            JwtUtil jwtUtil,
            JwtMapper jwtMapper,
            RefreshTokenService refreshTokenService) {

        this.otpService = otpService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.jwtMapper = jwtMapper;
        this.refreshTokenService = refreshTokenService;
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

    /**
     * Refreshes user session if called within the refresh time window (Case 1).
     * If called after the time window or with a revoked token, throws BadCredentialsException (Case 2).
     */
    public AuthResponseDto refreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BadCredentialsException("Refresh token cannot be empty");
        }

        // Validate and rotate refresh token. Throws BadCredentialsException if outside time window.
        String newRefreshToken = refreshTokenService.rotateRefreshToken(refreshToken);

        String phone = jwtUtil.extractPhoneNumber(newRefreshToken);
        User user = userService.getUserEntityByPhoneNumber(phone);

        JwtUserDto jwtUser = jwtMapper.toJwtUser(user);
        String newAccessToken = jwtUtil.generateToken(jwtUser, false);

        return AuthResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .userId(jwtUser.getUserId())
                .role(jwtUser.getRole())
                .registrationStatus(jwtUser.getRegistrationStatus())
                .phoneNumber(user.getPhoneNumber())
                .name(user.getName())
                .build();
    }

    public AuthResponseDto createAuthResponse(User user) {
        return buildAuthResponse(user);
    }

    /**
     * Returns current user session details for an existing user coming within access token time window (Case 1).
     */
    public AuthResponseDto getCurrentSession(User user) {
        JwtUserDto jwtUser = jwtMapper.toJwtUser(user);
        return AuthResponseDto.builder()
                .userId(jwtUser.getUserId())
                .role(jwtUser.getRole())
                .registrationStatus(jwtUser.getRegistrationStatus())
                .phoneNumber(user.getPhoneNumber())
                .name(user.getName())
                .build();
    }

    /**
     * Revokes session on logout.
     */
    public void logout(String refreshToken, User user) {
        if (refreshToken != null && !refreshToken.isBlank()) {
            refreshTokenService.revokeRefreshToken(refreshToken);
        } else if (user != null) {
            refreshTokenService.revokeAllTokens(user);
        }
    }

    private AuthResponseDto buildAuthResponse(User user) {
        JwtUserDto jwtUser = jwtMapper.toJwtUser(user);

        String accessToken = jwtUtil.generateToken(jwtUser, false);
        String refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(jwtUser.getUserId())
                .role(jwtUser.getRole())
                .registrationStatus(jwtUser.getRegistrationStatus())
                .phoneNumber(user.getPhoneNumber())
                .name(user.getName())
                .build();
    }
}
