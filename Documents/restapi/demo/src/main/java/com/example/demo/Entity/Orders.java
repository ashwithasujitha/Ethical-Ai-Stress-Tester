package com.example.demo.Entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


@Entity
public class Orders {
        @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private Long Orderdate;
    
    private String status;
    private String DeliveryPerson;
   
    @NotNull(message = "Order amount is required")
    @Min(value = 1, message = "Order amount must be at least 1")
    private Double TotalAmount;
    
    private User User;
    
   
    private Restaurant restaurant;
   
    private List<OrderItem>OrderItem;
    public Orders(){
        //DEfault
    }
   
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
   
    public Long getOrderdate() {
        return Orderdate;
    }
    public void setOrderdate(Long orderdate) {
        Orderdate = orderdate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Restaurant getRestaurant() {
        return restaurant;
    }
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
    
    
    public com.example.demo.Entity.User getUser() {
        return User;
    }
    public void setUser(com.example.demo.Entity.User user) {
        User = user;
    }
    public List<OrderItem> getOrderItem() {
        return OrderItem;
    }
    public void setOrderItem(List<OrderItem> orderItem) {
        OrderItem = orderItem;
    }
    public String getDeliveryPerson() {
        return DeliveryPerson;
    }

    public void setDeliveryPerson(String deliveryPerson) {
        DeliveryPerson = deliveryPerson;
    }
    public Double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        TotalAmount = totalAmount;
    }
}
