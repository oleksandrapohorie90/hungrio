package com.growthhungry.hungrio.service;

import com.growthhungry.hungrio.model.User;
import com.growthhungry.hungrio.service.dto.UserRegistrationDto;

import java.util.Optional;

public interface UserService {
    User registerUser(UserRegistrationDto userData);

    Optional<User> findByUsername(String username);
}
