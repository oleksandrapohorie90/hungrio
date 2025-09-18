package com.growthhungry.hungrio.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, world!";
    }
    /* @RestController means this class can handle HTTP requests and responses directly (the return value is sent as HTTP response body).
     @GetMapping("/hello") maps GET requests to /hello.
     */
}


