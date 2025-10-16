package com.growthhungry.hungrio;

import com.growthhungry.hungrio.controller.AuthController;
import com.growthhungry.hungrio.dto.UserRegistrationDto;
import com.growthhungry.hungrio.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // ⬅️ disables Spring Security filters for these tests
public class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    UserService userService;
    @MockitoBean
    PasswordEncoder passwordEncoder;

    @Test
    void register_success_returns201() throws Exception {
        when(userService.registerUser(any(UserRegistrationDto.class))).thenReturn(null);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"alex","password":"123"}"""))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    void register_invalidInput_returns400_withMessage() throws Exception {
        when(userService.registerUser(any(UserRegistrationDto.class)))
                .thenThrow(new IllegalArgumentException("Username already exists"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"alex","password":"123"}"""))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username already exists"));
    }

    @Test
    void login_success_returns200() throws Exception {
        var user = Mockito.mock(com.growthhungry.hungrio.model.User.class);
        when(user.getPasswordHash()).thenReturn("$hash$");
        when(userService.findByUsername("alex")).thenReturn(user);
        when(passwordEncoder.matches(eq("123"), eq("$hash$"))).thenReturn(true);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"alex","password":"123"}"""))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }

    @Test
    void login_wrongPassword_returns401() throws Exception {
        var user = Mockito.mock(com.growthhungry.hungrio.model.User.class);
        when(user.getPasswordHash()).thenReturn("$hash$");
        when(userService.findByUsername("alex")).thenReturn(user);
        when(passwordEncoder.matches(eq("bad"), eq("$hash$"))).thenReturn(false);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"alex","password":"bad"}"""))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }

    @Test
    void login_userNotFound_returns401() throws Exception {
        when(userService.findByUsername("ghost")).thenReturn(null);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"ghost","password":"whatever"}"""))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }
}
