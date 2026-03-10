
package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userrepo;

    public User userService(User u)
    {
        return userrepo.save(u);
    }
    public List<User>getAllUser()
    {
        return userrepo.findAll();
    }
    public Optional<User> getById(long id)
    {
        return userrepo.findById(id);
    }
    public User UpdateUser(Long id,User u)
    {
        u.setId(id);
        return userrepo.save(u);
    }
    public String DeleteById(long id)
    {
        userrepo.deleteById(id);
        return "Success";
    }
    
    public Page<User> getUserByPage(int page,int size)
    {
        PageRequest pageable=PageRequest.of(page, size);
        return userrepo.findAll(pageable);
    }
    public List<User>sortByUser()
    {
        return userrepo.findAll(Sort.by(Sort.Direction.ASC, "username"));
    }
    public List<User>getByQuery(String name)
    {
        return userrepo.findByUsername(name);
    }
    public List<User> getByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid name");
        }
        return userrepo.findByUsername(name);
    }


}
