package com.example.demo.dto;

import java.time.LocalDate;

public class updateUser {
      private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String about;
    private Boolean receiveCoupons;
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
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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
    
}
