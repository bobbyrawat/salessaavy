package com.example.salessaavy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.salessaavy.entity.User;
import com.example.salessaavy.services.OtpService;
import com.example.salessaavy.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;
    private final UserService userService;

    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(Authentication authentication) {

        String username = authentication.getName();

        User user = userService.findByUsername(username);

        if ("SELLER".equalsIgnoreCase(user.getRole())) {
            return ResponseEntity.ok("ALREADY_SELLER");
        }

        otpService.sendOtp(user.getEmail());

        return ResponseEntity.ok("OTP_SENT");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(
            Authentication authentication,
            @RequestParam String otp) {

        String username = authentication.getName();

        User user = userService.findByUsername(username);

        boolean valid = otpService.verifyOtp(user.getEmail(), otp);

        if (!valid) {
            return ResponseEntity.badRequest()
                    .body("INVALID_OTP");
        }

        userService.upgradeToSeller(user.getEmail());

        return ResponseEntity.ok("SELLER_UPGRADED");
        
    }
    
}