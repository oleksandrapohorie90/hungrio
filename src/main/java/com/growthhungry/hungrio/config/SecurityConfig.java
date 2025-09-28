package com.growthhungry.hungrio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        //PasswordEncoder is the Spring Security interface for one-way password hashing and verification.
        //default strength (log rounds) = 10; you can raise to 12 for stronger hashing
        //BCryptPasswordEncoder uses BCrypt with a per-password salt and a tunable cost factor (default 10). Higher strength = slower hashing = better brute-force resistance.
        return new BCryptPasswordEncoder();
        //This exposes a singleton BCrypt encoder in the Spring context for dependency injection anywhere (e.g., your service). BCrypt is the recommended one-way hashing algorithm in Spring Security.
    }

    @Bean
    SecurityFilterChain security(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/register", "/api/auth/login", "/hello").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }

}

