package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.RegisterRequestDto;
import com.example.demo.model.User;
import com.example.demo.repository.userLoginRepository;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.userService;

@RestController
@RequestMapping("/api/users")
//@CrossOrigin(origins="http://localhost:8080/")
public class userLoginController {
  


    private final userService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public userLoginController(
            userService userService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil) {

        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

   

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto dto) {

       User user = new User();
    user.setFirstName(dto.getFirstName());
    user.setLastName(dto.getLastName());
    user.setEmail(dto.getEmail());
    user.setPassword(passwordEncoder.encode(dto.getPassword()));
    user.setRole(User.Role.USER);

    userService.createUser(user);

    return ResponseEntity.ok("User registered successfully");
    }

    // ---------------- LOGIN ----------------
    @PostMapping("/login")
public ResponseEntity<Map<String, String>> login(
        @RequestBody LoginRequestDto dto) {

    String email = dto.getEmail();
    String password = dto.getPassword();

    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    email, password));

    String token = jwtUtil.generateToken(email);

    User user = userService.getUserByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    return ResponseEntity.ok(
            Map.of(
                    "token", token,
                    "userId", String.valueOf(user.getId()),
                    "email", user.getEmail(),
                    "role", user.getRole().name()
            )
    );
}



   @GetMapping("/me")
public ResponseEntity<User> myProfile(
        Authentication authentication) {

    String email = authentication.getName();

    User user = userService.getUserByEmail(email)
            .orElseThrow();

    return ResponseEntity.ok(user);
}

}



