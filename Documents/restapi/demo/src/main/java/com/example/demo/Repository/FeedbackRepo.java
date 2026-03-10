package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Entity.Feedback;


public interface  FeedbackRepo extends JpaRepository<Feedback,Long>{
    @Query("SELECT f FROM Feedback f WHERE f.id IN :ids")
    List<Feedback> findById(@Param("ids") List<Long> ids);
    

}

    
