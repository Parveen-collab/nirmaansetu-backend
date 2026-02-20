//Implement logic to generate, send (via a third-party SMS provider like Twilio or Firebase), and validate OTPs.

package com.nirmaansetu.backend.service;

import com.nirmaansetu.backend.entity.OtpEntity;
import com.nirmaansetu.backend.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private SmsService smsService; // 1. Injected SmsService

    public void sendOtp(String phoneNumber) {
        // Generate a 6-digit OTP
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        OtpEntity otpEntity = otpRepository.findByPhoneNumber(phoneNumber)
                .orElse(new OtpEntity());

        otpEntity.setPhoneNumber(phoneNumber);
        otpEntity.setOtp(otp);
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        // Save OTP to database
        otpRepository.save(otpEntity);

        // 2. Call SmsService to send the actual SMS via Twilio
        String messageBody = "Your NirmaanSetu OTP is: " + otp + ". Valid for 5 minutes.";
        smsService.sendSms(phoneNumber, messageBody);
    }

    public boolean verifyOtp(String phoneNumber, String otp) {
        return otpRepository.findByPhoneNumber(phoneNumber)
                .map(entity -> entity.getOtp().equals(otp) &&
                        entity.getExpiryTime().isAfter(LocalDateTime.now()))
                .orElse(false);
    }
}
