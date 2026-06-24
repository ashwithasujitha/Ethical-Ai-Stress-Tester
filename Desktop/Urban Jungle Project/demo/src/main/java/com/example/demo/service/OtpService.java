package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class OtpService {

    private final Map<String, String> otpStorage = new HashMap<>();
    private final Map<String, LocalDateTime> otpExpiry = new HashMap<>();

    public String generateOtp(String email) {
        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
        otpStorage.put(email, otp);
        otpExpiry.put(email, LocalDateTime.now().plusMinutes(5));
        System.out.println("OTP for " + email + " = " + otp);
        return otp;
    }

    public boolean validateOtp(String email, String otp) {

        if (!otpStorage.containsKey(email)) {
            return false;
        }

        if (otpExpiry.get(email).isBefore(LocalDateTime.now())) {
            otpStorage.remove(email);
            otpExpiry.remove(email);
            return false;
        }

        boolean isValid = otpStorage.get(email).equals(otp);

        if (isValid) {
            otpStorage.remove(email);
            otpExpiry.remove(email);
        }

        return isValid;
    }
}
