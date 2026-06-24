package com.example.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins="http://localhost:8080/")
public class Tesstcontroller {
    @GetMapping("/test")
    public String test() {
        return "Backend Connected Successfully";
    }

}
