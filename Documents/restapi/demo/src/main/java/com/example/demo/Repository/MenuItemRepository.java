package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Entity.MenuItem;

public class MenuItemRepository {
    public interface MenuRepo extends JpaRepository<MenuItem, Long> {
    @Query("SELECT m from MenuItem m where m.name LIKE %:name%")
    List<MenuItem>findByName(@Param("name")String name);
}

}
