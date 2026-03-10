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

import com.example.demo.Entity.Feedback;
import com.example.demo.Service.FeedbackService;

public class FeedbackController {
        @Autowired
    FeedbackService fs;

        @PostMapping("/postfb")
    public Feedback insertUser(@RequestBody Feedback m)
    {
        return fs.createfeed(m);
    }

    @GetMapping("/getfb")
    public List<Feedback>getAllMenu()
    {
        return fs.getAllfeed();
    }

    @GetMapping("/deletefb/{id}")
    public Optional<Feedback> getMenuById(@PathVariable Long id)
    {
        return fs.getByIdfeed(id);
    }

    @DeleteMapping("/deletefb/{id}")
    public String deleteMenu(@PathVariable Long id)
    {
        return fs.deleteByIdfeed(id);
    }
    
    @PutMapping("/bc/{id}")
    public Feedback updateMenuItem(@PathVariable Long id,@RequestBody Feedback  u)
    {
        return fs.updatefeed(id,u);
    }
    @GetMapping("/page")
    public Page<Feedback> getByPage(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "5") int size) {
        return fs.getFeedbacksByPage(page, size);
    }

    @GetMapping("/sort")
    public List<Feedback> sortByOrderDate() { 
        return fs.sortByFeedbackDate();
    }

    @GetMapping("/fbById/{id}")
    public List<Feedback> getByOrderId(@PathVariable Long id) {
        return fs.getByFeedbackId(id);
    }
    
}


