package com.nirmaansetu.backend.modules.auth.service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${twilio.phone-number}")
    private String fromPhoneNumber;

    @Async
    public void sendSms(String toPhoneNumber, String messageBody) {
        try {
            // remove spaces
            toPhoneNumber = toPhoneNumber.trim().replaceAll(" ", "");

            // add + if missing
            if (!toPhoneNumber.startsWith("+")) {
                toPhoneNumber = "+" + toPhoneNumber;
            }

            Message message = Message.creator(
                    new PhoneNumber(toPhoneNumber),
                    new PhoneNumber(fromPhoneNumber),
                    messageBody
            ).create();

            System.out.println("SMS sent: " + message.getSid());
        } catch (Exception e) {
            System.err.println("Failed to send SMS to " + toPhoneNumber + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}