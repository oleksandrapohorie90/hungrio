package com.growthhungry.hungrio.controller;

import com.growthhungry.hungrio.dto.UserLoginDto;
import com.growthhungry.hungrio.dto.UserRegistrationDto;
import com.growthhungry.hungrio.model.User;
import com.growthhungry.hungrio.service.UserService;
import jakarta.validation.Valid;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // -------------------------
    // Registration endpoint
    // -------------------------
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRegistrationDto registrationDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        
        try {
            userService.registerUser(registrationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // -------------------------
    // Login endpoint
    // -------------------------
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginDto loginDto) {
        User user = userService.findByUsername(loginDto.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        boolean passwordMatches = passwordEncoder.matches(loginDto.getPassword(), user.getPasswordHash());
        if (!passwordMatches) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        return ResponseEntity.ok("Login successful");
    }
}