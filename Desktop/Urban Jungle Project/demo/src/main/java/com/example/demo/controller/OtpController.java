package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.EmailService;
import com.example.demo.service.OtpService;

@RestController
public class OtpController {

    private final OtpService otpService;
    private final EmailService emailService;

    public OtpController(OtpService otpService, EmailService emailService) {
        this.otpService = otpService;
        this.emailService = emailService;
    }

    @PostMapping("/api/otp/send")
    public String sendOtp(@RequestParam String email) {
        String otp = otpService.generateOtp(email);
        emailService.sendOtpEmail(email, otp);
        return "OTP sent successfully";
    }

    @PostMapping("/api/otp/verify")
    public boolean verifyOtp(
            @RequestParam String email,
            @RequestParam String otp) {
        return otpService.validateOtp(email, otp);
    }
}
