package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
    @Query("SELECT r from Restaurant r where r.name LIKE %:name%")
   List<Restaurant>findByName(@Param("name")String name);
   
   
}

