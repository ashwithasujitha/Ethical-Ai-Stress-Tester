package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.example.demo.Entity.MenuItem;
import com.example.demo.Repository.MenuItemRepository.MenuRepo;

public class MenuItemService {
        @Autowired
    MenuRepo menurepo;
    
    public MenuItem menuItem(MenuItem m) {
        return menurepo.save(m);
    }
    
    public List<MenuItem> getAllmenu() {
        return menurepo.findAll();
    }
    
    public Optional<MenuItem> getById(Long id) {
        return menurepo.findById(id);
    }
    
    public MenuItem updateMenuItem(Long id, MenuItem m) {
        m.setId(id);
        return menurepo.save(m);
    }
    
    public String deleteById(long id) {
        menurepo.deleteById(id);
        return "Success";
    }
    
    public Page<MenuItem> getMenuItemsByPage(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return menurepo.findAll(pageable);
    }
    
    // Sorting by name instead of orderDate
    public List<MenuItem> sortByName() {
        return menurepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }
    
    // Retrieving menu items by name instead of ID
    public List<MenuItem> getByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid name");
        }
        return menurepo.findByName(name);
    }

}
