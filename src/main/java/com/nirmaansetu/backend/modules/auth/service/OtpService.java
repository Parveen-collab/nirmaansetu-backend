package com.nirmaansetu.backend.modules.auth.service;

import com.nirmaansetu.backend.modules.auth.exception.RateLimitException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Service class for handling OTP (One-Time Password) generation, verification, and rate limiting.
 * Uses Redis for storage and SMS for delivery.
 */
@Service
public class OtpService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    // Maximum number of OTP requests allowed before locking
    private static final int MAX_OTP_ATTEMPTS = 5;
    // Duration for which a phone number is locked after exceeding attempts
    private static final int LOCK_TIME_MINUTES = 10;
    // OTP validity duration
    private static final int OTP_EXPIRY_MINUTES = 5;
    // How long the "verified" status remains valid for registration
    private static final int VERIFIED_PHONE_EXPIRY_MINUTES = 10;

    @Autowired
    private SmsService smsService;

    /**
     * Generates and sends a 4-digit OTP to the specified phone number.
     * Implements rate limiting to prevent abuse.
     * 
     * @param phoneNumber The recipient's phone number
     * @throws RateLimitException if the user has exceeded the maximum OTP attempts
     */
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
        String hashedOtp = hashOtp(otp);
        ops.set(otpKey, hashedOtp, OTP_EXPIRY_MINUTES, TimeUnit.MINUTES);

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

    /**
     * Verifies the provided OTP against the hashed version stored in Redis.
     * If valid, marks the phone number as verified for a limited time.
     * 
     * @param phoneNumber The phone number being verified
     * @param otp The OTP provided by the user
     * @return true if verification is successful, false otherwise
     */
    public boolean verifyOtp(String phoneNumber, String otp) {
        String otpKey = "otp:" + phoneNumber;
        String verifiedKey = "verified_phone:" + phoneNumber;
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String storedOtpHash = ops.get(otpKey);

        if (storedOtpHash != null && storedOtpHash.equals(hashOtp(otp))) {
            redisTemplate.delete(otpKey); // Prevent reuse
            // Store verification status for registration
            ops.set(verifiedKey, "true", VERIFIED_PHONE_EXPIRY_MINUTES, TimeUnit.MINUTES);
            return true;
        }
        return false;
    }

    /**
     * Checks if a phone number has been successfully verified recently.
     * 
     * @param phoneNumber The phone number to check
     * @return true if the phone number is verified, false otherwise
     */
    public boolean isPhoneNumberVerified(String phoneNumber) {
        String verifiedKey = "verified_phone:" + phoneNumber;
        return redisTemplate.hasKey(verifiedKey);
    }

    /**
     * Clears the verification status for a phone number (e.g., after successful registration).
     * 
     * @param phoneNumber The phone number to clear verification for
     */
    public void clearVerification(String phoneNumber) {
        String verifiedKey = "verified_phone:" + phoneNumber;
        redisTemplate.delete(verifiedKey);
    }

    /**
     * Hashes the OTP using SHA-256 for secure storage in Redis.
     * 
     * @param otp The plain text OTP
     * @return Base64 encoded hash string
     */
    private String hashOtp(String otp) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(otp.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing OTP", e);
        }
    }
}
