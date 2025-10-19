package com.growthhungry.hungrio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growthhungry.hungrio.controller.AuthController;
import com.growthhungry.hungrio.dto.UserRegistrationDto;
import com.growthhungry.hungrio.model.User;
import com.growthhungry.hungrio.service.UserService;
import com.growthhungry.hungrio.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;


    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    //Register tests
    @Test
    void register_Successful() throws Exception {
        UserRegistrationDto dto = new UserRegistrationDto("newUser", "password123");
        when(userService.registerUser(any(UserRegistrationDto.class))).thenReturn(new User());

        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    void register_UsernameAlreadyExists() throws Exception {
        UserRegistrationDto dto = new UserRegistrationDto("existingUser", "password123");
        doThrow(new IllegalArgumentException("Username already exists"))
                .when(userService).registerUser(any(UserRegistrationDto.class));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username already exists"));
    }

    //login tests
    @Test
    void login_Successful() throws Exception {
        UserRegistrationDto dto = new UserRegistrationDto("testUser", "password123");
        User user = new User();
        user.setUsername("testUser");
        user.setPasswordHash("encodedPassword");

        when(userService.findByUsername("testUser")).thenReturn(user);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }

    @Test
    void login_IncorrectPassword() throws Exception {
        UserRegistrationDto dto = new UserRegistrationDto("testUser", "wrongPass");
        User user = new User();
        user.setUsername("testUser");
        user.setPasswordHash("encodedPassword");

        when(userService.findByUsername("testUser")).thenReturn(user);
        when(passwordEncoder.matches("wrongPass", "encodedPassword")).thenReturn(false);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }

    @Test
    void login_UserNotFound_Returns401() throws Exception {
        when(userService.findByUsername("ghost")).thenReturn(null);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"ghost","password":"whatever"}"""))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }

    @Test
    void register_EmptyUsername_Returns400() throws Exception {
        String body = """
                {"username":"","password":"password123"}
                """;
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_EmptyPassword_returns400() throws Exception {
        String body = """
                {"username":"alex","password":""}
                """;
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_ShortUsername() throws Exception {
        UserRegistrationDto dto = new UserRegistrationDto("ab", "password123");
        
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("username: Username must be between 3 and 50 characters"));
    }

    @Test
    void register_ShortPassword() throws Exception {
        UserRegistrationDto dto = new UserRegistrationDto("testuser", "12345");
        
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("password: Password must be at least 6 characters long"));
    }

}
