package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.OtpGeneration;


@Repository
public interface OtpRepository extends JpaRepository<OtpGeneration, Long> {
    OtpGeneration findByEmail(String email);
    
    Optional<OtpGeneration> findByEmailAndOtpAndVerifiedFalse(String email, String otp);
} 
