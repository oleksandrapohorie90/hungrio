package com.growthhungry.hungrio.service;

import com.growthhungry.hungrio.model.User;
import com.growthhungry.hungrio.repository.UserRepository;
import com.growthhungry.hungrio.service.dto.UserRegistrationDto;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service // marks business/service layer for component scan
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public User registerUser(UserRegistrationDto userData) {
        String username = userData.getUsername();
        String rawPassword = userData.getPassword();

        if (repo.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        User u = new User();
        u.setUsername(username);
        u.setPasswordHash(encoder.encode(rawPassword)); // hash before save
        return repo.save(u);
    }

    @Override
    @Transactional()
    public Optional<User> findByUsername(String username) {
        return repo.findByUsername(username);
    }
}