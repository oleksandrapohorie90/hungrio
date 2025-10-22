package com.growthhungry.hungrio.service;

import com.growthhungry.hungrio.dto.UserRegistrationDto;
import com.growthhungry.hungrio.model.User;
import com.growthhungry.hungrio.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service // marks business/service layer for component scan
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    

    @Override
    public User registerUser(UserRegistrationDto userData) throws IllegalArgumentException{
        //check if username already exists

        if (userRepository.findByUsername(userData.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists");
        }

        //hash password
        String hashedPassword = passwordEncoder.encode(userData.getPassword());

       //create entity and save
        User user = new User(userData.getUsername(),hashedPassword);
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}