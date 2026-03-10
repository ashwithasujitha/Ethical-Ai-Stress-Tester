package com.example.demo.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Entity.MenuItem;
import com.example.demo.Service.MenuItemService;

public class MenuItemController {
        
    @Autowired
    MenuItemService menuservice;

    @PostMapping("/mpost")
    public MenuItem insertMenuItem(@RequestBody MenuItem m) {
        return menuservice.menuItem(m);
    }

    @GetMapping("/mget")
    public List<MenuItem> getAllMenu() {
        return menuservice.getAllmenu();
    }

    @GetMapping("/menu/{id}")
    public Optional<MenuItem> getMenuById(@PathVariable Long id) {
        return menuservice.getById(id);
    }

    @DeleteMapping("/menu/{id}")
    public String deleteMenu(@PathVariable Long id) {
        return menuservice.deleteById(id);
    }

    @PutMapping("/menu/{id}")
    public MenuItem updateMenuItem(@PathVariable Long id, @RequestBody MenuItem m) {
        return menuservice.updateMenuItem(id, m);
    }

    @GetMapping("/page")
    public Page<MenuItem> getByPage(@RequestParam(defaultValue = "0") int page, 
                                    @RequestParam(defaultValue = "5") int size) {
        return menuservice.getMenuItemsByPage(page, size);
    }

    @GetMapping("/sort")
    public List<MenuItem> sortByName() {
        return menuservice.sortByName();
    }

    @GetMapping("/menuByName/{name}")
    public List<MenuItem> getByName(@PathVariable String name) {
        return menuservice.getByName(name);
    }

}
