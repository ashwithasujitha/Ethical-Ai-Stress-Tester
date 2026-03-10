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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.User;
import com.example.demo.Service.UserService;

@RestController
@RequestMapping("/userapi")
public class UserController {
    @Autowired
    UserService userservice;

        @PostMapping("/post")
    public User insertUser(@RequestBody User u)
    {
        return userservice.userService(u);
    }

    @GetMapping("/get")
    public List<User>getAllUser()
    {
        return userservice.getAllUser();
    }

    @GetMapping("/user/{id}")
    public Optional<User> getUserById(@PathVariable Long id)
    {
        return userservice.getById(id);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id)
    {
        return userservice.DeleteById(id);
    }
    
    @PutMapping("/Users/{id}")
    public User updateUser(@PathVariable Long id,@RequestBody User u)
    {
        return userservice.UpdateUser(id,u);
    }
    @GetMapping("/page")
    public Page<User> getByPage(@RequestParam (defaultValue="0") int page,@RequestParam (defaultValue = "5") int size)
    {
        return userservice.getUserByPage(page, size);
    }
    @GetMapping("/sort")
    public List<User>sortByUser()
    {
        return userservice.sortByUser();
    }
    @GetMapping("/q/{name}")
    public List<User>getUsername(@PathVariable String name)
    {
        return userservice.getByQuery(name);
    }
    @GetMapping("/custom/{username}")
    public List<User>findByUserName(@PathVariable String username)
    {
        return userservice.getByName(username);
    }
    
}
