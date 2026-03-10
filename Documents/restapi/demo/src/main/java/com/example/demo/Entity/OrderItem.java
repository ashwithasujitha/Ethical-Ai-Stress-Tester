package com.example.demo.Entity;





import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import jakarta.validation.constraints.NotNull;


@Entity
public class OrderItem {
   @Id
   
 
   private Long id;
   @NotNull(message = "Quantity is required")
   private long quantity;
   
   private Orders Orders;
   public Double getPrice() {
      return price;
   }
   public void setPrice(Double price) {
      this.price = price;
   }

   private Double price;
 
   public OrderItem(){
      //Default
   }
   public Long getId() {
       return id;
   }
 
   public void setId(Long id) {
       this.id = id;
   }
 
    public long getQuantity() {
       return quantity;
   }
 
    public void setQuantity(long quantity) {
       this.quantity = quantity;
   }

   public Orders getOrders() {
      return Orders;
   }

   public void setOrders(Orders orders) {
      Orders = orders;
   }
}
