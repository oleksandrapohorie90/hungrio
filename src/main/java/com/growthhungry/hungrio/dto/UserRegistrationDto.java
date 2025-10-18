package com.growthhungry.hungrio.dto;
import jakarta.validation.constraints.NotBlank;

public class UserRegistrationDto {

    @NotBlank(message = "Username must not be blank")
    public String username;
    @NotBlank(message = "Password must not be blank")
    public String password;

    public UserRegistrationDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserRegistrationDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
