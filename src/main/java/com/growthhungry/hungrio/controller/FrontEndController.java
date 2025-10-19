package com.growthhungry.hungrio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import com.growthhungry.hungrio.dto.UserRegistrationDto;

@Controller
public class FrontEndController {
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("userRegistrationDto")) {
            model.addAttribute("userRegistrationDto", new UserRegistrationDto());
        }
        return "register";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
    
}
