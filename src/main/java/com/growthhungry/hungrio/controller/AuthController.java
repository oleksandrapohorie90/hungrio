package com.growthhungry.hungrio.controller;

import com.growthhungry.hungrio.controller.dto.LoginRequest;
import com.growthhungry.hungrio.controller.dto.RegisterRequest;
import com.growthhungry.hungrio.model.User;
import com.growthhungry.hungrio.service.UserService;
import com.growthhungry.hungrio.service.dto.UserRegistrationDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest body) {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername(body.username);
        dto.setPassword(body.password);

        userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest body) {
        Optional<User> maybe = userService.findByUsername(body.username);
        if (maybe.isPresent()) {
            User u = maybe.get();
            if (passwordEncoder.matches(body.password, u.getPasswordHash())) {
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
