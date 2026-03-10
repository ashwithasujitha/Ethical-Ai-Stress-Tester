package com.example.demo.Entity;





import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
public class Feedback {
    @Id
    private Long id;
    private String content;

    // private Restaurant restaurant;
    private String User;
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;
    private String comment;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Feedback(){
        //Default
    }
    
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
   
    public String getUser() {
        return User;
    }
    public void setUser(String user) {
        User = user;
    }
    // public Restaurant getRestaurant() {
    //     return restaurant;
    // }
    // public void setRestaurant(Restaurant restaurant) {
    //     this.restaurant = restaurant;
    // }

}
