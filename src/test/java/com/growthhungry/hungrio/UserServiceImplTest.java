package com.growthhungry.hungrio;

import com.growthhungry.hungrio.dto.UserRegistrationDto;
import com.growthhungry.hungrio.model.User;
import com.growthhungry.hungrio.repository.UserRepository;
import com.growthhungry.hungrio.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    private static UserRegistrationDto dto(String u, String p) {
        return new UserRegistrationDto(u, p);
    }

    // 1) registerUser: successful registration
    @Test
    void registerUser_success() {
        when(userRepository.findByUsername("alex")).thenReturn(null); // username is free
        when(passwordEncoder.encode("123")).thenReturn("ENC");         // encode password
        when(userRepository.save(any(User.class)))
                .thenReturn(new User("alex", "ENC"));                  // repo returns saved user

        User result = userService.registerUser(dto("alex", "123"));

        assertNotNull(result);
        assertEquals("alex", result.getUsername());
        assertEquals("ENC", result.getPasswordHash());
        verify(userRepository).findByUsername("alex");
        verify(passwordEncoder).encode("123");
        verify(userRepository).save(any(User.class));
    }

    // 2) registerUser: existing username -> throw IllegalArgumentException
    @Test
    void registerUser_existingUsername_throws() {
        when(userRepository.findByUsername("alex"))
                .thenReturn(new User("alex", "whatever"));             // already taken

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(dto("alex", "123"))
        );
        assertEquals("Username already exists", ex.getMessage());
        verify(userRepository).findByUsername("alex");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
    }

    // 3) findByUsername: existing user
    @Test
    void findByUsername_found() {
        var user = new User("alex", "pw");
        when(userRepository.findByUsername("alex")).thenReturn(user);

        User result = userService.findByUsername("alex");

        assertSame(user, result);
        verify(userRepository).findByUsername("alex");
        verifyNoInteractions(passwordEncoder);
    }

    // 4) findByUsername: non-existent user -> null
    @Test
    void findByUsername_notFound_returnsNull() {
        when(userRepository.findByUsername("nobody")).thenReturn(null);

        User result = userService.findByUsername("nobody");

        assertNull(result);
        verify(userRepository).findByUsername("nobody");
        verifyNoInteractions(passwordEncoder);
    }
}
