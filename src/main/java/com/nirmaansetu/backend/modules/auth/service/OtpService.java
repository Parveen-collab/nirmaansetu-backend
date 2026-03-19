//Implement logic to generate, send (via a third-party SMS provider like Twilio or Firebase), and validate OTPs.

package com.nirmaansetu.backend.modules.auth.service;

import com.nirmaansetu.backend.exception.RateLimitException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import com.nirmaansetu.backend.modules.auth.entity.OtpEntity;
import com.nirmaansetu.backend.modules.auth.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final int MAX_OTP_ATTEMPTS = 5;
    private static final int LOCK_TIME_MINUTES = 10;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private SmsService smsService; // 1. Injected SmsService

    public void sendOtp(String phoneNumber) {
        String key = "otp_limit:" + phoneNumber;
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        String currentCount = ops.get(key);

        if (currentCount != null && Integer.parseInt(currentCount) >= MAX_OTP_ATTEMPTS) {
            throw new RateLimitException("OTP limit exceeded. Please try again after 10 minutes.");
        }

        // Generate a 4-digit OTP
        String otp = String.valueOf(new Random().nextInt(9000) + 1000);

        OtpEntity otpEntity = otpRepository.findByPhoneNumber(phoneNumber)
                .orElse(new OtpEntity());

        otpEntity.setPhoneNumber(phoneNumber);
        otpEntity.setOtp(otp);
        otpEntity.setExpiryTime(Instant.from(LocalDateTime.now().plusMinutes(5)));

        // Universal Checking: Increment count in Redis
        if (currentCount == null) {
            ops.set(key, "1", LOCK_TIME_MINUTES, TimeUnit.MINUTES);
        } else {
            ops.increment(key);
        }

        // Save OTP to database
        otpRepository.save(otpEntity);

        // 2. Call SmsService to send the actual SMS via Twilio
        String messageBody = "Your NirmaanSetu OTP is: " + otp + ". Valid for 5 minutes.";
        smsService.sendSms(phoneNumber, messageBody);
    }

    @Cacheable(value = "otp", key = "#phoneNumber")
    public boolean verifyOtp(String phoneNumber, String otp) {
        return otpRepository.findByPhoneNumber(phoneNumber)
                .map(entity -> entity.getOtp().equals(otp) &&
                        entity.getExpiryTime().isAfter(Instant.from(LocalDateTime.now())))
                .orElse(false);
    }
}
