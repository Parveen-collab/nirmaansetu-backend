package com.nirmaansetu.backend.modules.auth.service;

import com.nirmaansetu.backend.modules.auth.exception.RateLimitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OtpServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private SmsService smsService;

    @InjectMocks
    private OtpService otpService;

    private final String phoneNumber = "1234567890";

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testSendOtp_Success() {
        when(valueOperations.get(anyString())).thenReturn(null);

        otpService.sendOtp(phoneNumber);

        verify(valueOperations).set(startsWith("otp:"), anyString(), eq(5L), eq(TimeUnit.MINUTES));
        verify(valueOperations).set(startsWith("otp_limit:"), eq("1"), eq(10L), eq(TimeUnit.MINUTES));
        verify(smsService).sendSms(eq(phoneNumber), anyString());
    }

    @Test
    void testSendOtp_RateLimitExceeded() {
        when(valueOperations.get(anyString())).thenReturn("5");

        assertThrows(RateLimitException.class, () -> otpService.sendOtp(phoneNumber));
    }

    @Test
    void testVerifyOtp_Success() throws Exception {
        String otp = "1234";
        java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(otp.getBytes());
        String hashedOtp = java.util.Base64.getEncoder().encodeToString(hash);

        when(valueOperations.get("otp:" + phoneNumber)).thenReturn(hashedOtp);

        boolean result = otpService.verifyOtp(phoneNumber, otp);

        assertTrue(result);
        verify(redisTemplate).delete("otp:" + phoneNumber);
        verify(valueOperations).set(eq("verified_phone:" + phoneNumber), eq("true"), eq(10L), eq(TimeUnit.MINUTES));
    }

    @Test
    void testVerifyOtp_Failure() {
        when(valueOperations.get(anyString())).thenReturn("wrong_hash");

        boolean result = otpService.verifyOtp(phoneNumber, "1234");

        assertFalse(result);
    }

    @Test
    void testIsPhoneNumberVerified() {
        when(redisTemplate.hasKey("verified_phone:" + phoneNumber)).thenReturn(true);

        boolean result = otpService.isPhoneNumberVerified(phoneNumber);

        assertTrue(result);
    }

    @Test
    void testClearVerification() {
        otpService.clearVerification(phoneNumber);

        verify(redisTemplate).delete("verified_phone:" + phoneNumber);
    }
}
