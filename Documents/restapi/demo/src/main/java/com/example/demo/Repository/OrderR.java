package com.example.demo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.example.demo.Entity.Orders;

public interface OrderR extends JpaRepository<Orders, Long> {

    // Optional: Custom Query to find Order by ID (if needed)
    @Query("SELECT o FROM Orders o WHERE o.id = :id")
    Optional<Orders> findById(Long id);

}
