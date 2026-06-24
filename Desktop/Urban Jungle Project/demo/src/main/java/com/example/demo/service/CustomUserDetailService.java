package com.example.demo.service;

import com.example.demo.model.User;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.demo.repository.userLoginRepository;

@Component
@Service
public class CustomUserDetailService  implements UserDetailsService {
    
    @Autowired
    userLoginRepository ur;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user=ur.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
       
    
    return new org.springframework.security.core.userdetails.User(
                user.getEmail(),//
                user.getPassword(),
                Collections.singleton(
                    new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
                )
    );
    
            }
    
}
