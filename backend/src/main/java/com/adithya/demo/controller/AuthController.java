package com.adithya.demo.controller;

import com.adithya.demo.dto.*;
import com.adithya.demo.entity.User;
import com.adithya.demo.enums.Role;
import com.adithya.demo.repository.UserRepository;
import com.adithya.demo.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest req) {
        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        return new AuthResponse(jwtUtil.generateToken(user.getUsername()));
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) {
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("invalid credentials"));
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword()))
            throw new RuntimeException("invalid credentials");
        return new AuthResponse(jwtUtil.generateToken(user.getUsername()));
    }
}