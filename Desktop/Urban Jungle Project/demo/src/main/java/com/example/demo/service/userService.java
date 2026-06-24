package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Optional;

import com.example.demo.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.updateUser;
import com.example.demo.repository.userLoginRepository;
@Service
//@Autowired
public class userService {
    public final userLoginRepository userLoginRepository;
     public userService(userLoginRepository userLoginRepository) {
        this.userLoginRepository = userLoginRepository;

     }
     @Autowired
     private OtpService otpService;
     @Autowired
     private EmailService emailService;
     public User createUser(User user) {
     String otp=otpService.generateOtp(user.getEmail());
       user.setOtp(otp);
       user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
         user.setVerified(false);
        // user.setRole(User.Role.USER);


       System.out.println("OTP GENERATED = " + otp);

        User savedUser = userLoginRepository.save(user);

       
        emailService.sendOtpEmail(user.getEmail(), otp);

        return savedUser;

     }  
    public Optional< User> getUserByEmail(String email) {
        return userLoginRepository.findByEmail(email);
     }
    
      public User getUserById(Long id) {
        return userLoginRepository.findById(id).orElse(null);
     }
    //  public User getUserByName(User u) {
    //     return userLoginRepository.findByUsername(u.getFirstName()).orElse(null);
    //  }
    
     public User updateUser(Long id, updateUser dto) {

    User u = userLoginRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));

    u.setFirstName(dto.getFirstName());
    u.setLastName(dto.getLastName());
    u.setDateOfBirth(dto.getDateOfBirth());
    u.setAbout(dto.getAbout());
    u.setReceiveCoupons(dto.getReceiveCoupons());

    return userLoginRepository.save(u);
}


}
