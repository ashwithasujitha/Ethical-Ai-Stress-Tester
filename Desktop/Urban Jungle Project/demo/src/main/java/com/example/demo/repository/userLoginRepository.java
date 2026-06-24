package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface userLoginRepository extends JpaRepository<User, Long> {
     Optional<User> findByEmail(String email);
     //  Optional<User>  findByUsername(String name);
     
      
      // List findAll();
}
