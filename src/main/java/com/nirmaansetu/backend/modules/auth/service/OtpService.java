package com.nirmaansetu.backend.modules.auth.service;

import com.nirmaansetu.backend.modules.auth.exception.RateLimitException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final int MAX_OTP_ATTEMPTS = 5;
    private static final int LOCK_TIME_MINUTES = 10;
    private static final int OTP_EXPIRY_MINUTES = 5;

    @Autowired
    private SmsService smsService;

    public void sendOtp(String phoneNumber) {
        String limitKey = "otp_limit:" + phoneNumber;
        String otpKey = "otp:" + phoneNumber;
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        String currentCount = ops.get(limitKey);

        if (currentCount != null && Integer.parseInt(currentCount) >= MAX_OTP_ATTEMPTS) {
            throw new RateLimitException("OTP limit exceeded. Please try again after 10 minutes.");
        }

        // Generate a 4-digit OTP
        String otp = String.valueOf(new Random().nextInt(9000) + 1000);

        // Save OTP to Redis
        ops.set(otpKey, otp, OTP_EXPIRY_MINUTES, TimeUnit.MINUTES);

        // Rate limit handling
        if (currentCount == null) {
            ops.set(limitKey, "1", LOCK_TIME_MINUTES, TimeUnit.MINUTES);
        } else {
            ops.increment(limitKey);
        }

        // Send SMS
        String messageBody = "Your NirmaanSetu OTP is: " + otp + ". Valid for " + OTP_EXPIRY_MINUTES + " minutes.";
        smsService.sendSms(phoneNumber, messageBody);
    }

    public boolean verifyOtp(String phoneNumber, String otp) {
        String otpKey = "otp:" + phoneNumber;
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String storedOtp = ops.get(otpKey);

        if (storedOtp != null && storedOtp.equals(otp)) {
            redisTemplate.delete(otpKey); // Prevent reuse
            return true;
        }
        return false;
    }
}
