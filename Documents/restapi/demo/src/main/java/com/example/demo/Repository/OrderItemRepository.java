package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.example.demo.Entity.OrderItem;

public interface  OrderItemRepository extends JpaRepository<OrderItem,Long> {
    @Query("SELECT o FROM OrderItem o WHERE o.id IN :ids")
    List<OrderItem> findAllById(List<Long> ids);

}

