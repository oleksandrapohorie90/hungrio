package com.growthhungry.hungrio.service;


import com.growthhungry.hungrio.dto.UserRegistrationDto;
import com.growthhungry.hungrio.model.User;

public interface UserService {
    User registerUser(UserRegistrationDto userData) throws IllegalArgumentException;
    User findByUsername(String username);
}
