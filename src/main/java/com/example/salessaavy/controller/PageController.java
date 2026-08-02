package com.example.salessaavy.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PageController {

    @GetMapping("/")
    public String home() {
        return "Welcome to Home Page";
    }
@PostMapping("/test")
public String test() {
    System.out.println("TEST POST HIT");
    return "POST WORKING";
}
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication) {

        return "Welcome "
                + authentication.getName()
                + " to Dashboard";
    }
}