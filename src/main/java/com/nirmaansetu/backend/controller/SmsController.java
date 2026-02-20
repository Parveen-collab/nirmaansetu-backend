package com.nirmaansetu.backend.controller;

import com.nirmaansetu.backend.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @PostMapping("/send")
    public String sendSms(@RequestParam String phone) {

        smsService.sendSms(phone, "Welcome to NirmaanSetu!");

        return "SMS sent successfully";
    }
}