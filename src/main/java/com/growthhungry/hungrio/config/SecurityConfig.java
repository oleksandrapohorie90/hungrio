package com.growthhungry.hungrio.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

@Bean
SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        //Disables CSRF for APIs & H2 console.
        .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**", "/api/**"))
        .headers(h -> h.frameOptions(f -> f.sameOrigin()))

        //Returns a clean 401 JSON instead of redirect.
        .exceptionHandling(e -> e.authenticationEntryPoint((req, res, ex) -> {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.setContentType("application/json");
            res.getWriter().write("{\"error\":\"Unauthorized\"}");
        }))

        //Reuses your WebCorsConfig (so your frontend can call /api/chat).
        .cors(Customizer.withDefaults())

        //Ensures Spring doesn’t store sessions — JWT only.
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        //Opens /api/auth/**, protects /api/chat.
        .authorizeHttpRequests(auth -> auth
            // Public endpoints (login, register, H2 console)
            .requestMatchers("/api/auth/**", "/h2-console/**").permitAll()

            // Chat endpoint — must be authenticated
            .requestMatchers(HttpMethod.POST, "/api/chat").authenticated()

            // Everything else — optional, you can secure more later
            .anyRequest().permitAll()
        );

    //Inserts your JwtAuthFilter into the chain.
    http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}

}
