package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    private String password;

    private LocalDate dateOfBirth;

    private String facebookId;

    private String twitterId;

    @Enumerated(EnumType.STRING)
    private LoginProvider loginProvider;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String about;

    private Boolean receiveCoupons = false;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean active = true;

   @Column(name = "otp")
private String otp;

@Column(name = "otp_expiry")
private LocalDateTime otpExpiry;

@Column(name = "verified")
private boolean verified = false;


    public enum LoginProvider {
        NORMAL,
        FACEBOOK,
        TWITTER
    }

    public enum Role {
        USER,
        ADMIN
    }


    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    public LoginProvider getLoginProvider() {
        return loginProvider;
    }

    public void setLoginProvider(LoginProvider loginProvider) {
        this.loginProvider = loginProvider;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role)
    {
        this.role = role;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Boolean getReceiveCoupons() {
        return receiveCoupons;
    }

    public void setReceiveCoupons(Boolean receiveCoupons) {
        this.receiveCoupons = receiveCoupons;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getOtpExpiry() {
        return otpExpiry;
    }

    public void setOtpExpiry(LocalDateTime otpExpiry) {
        this.otpExpiry = otpExpiry;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    
}
