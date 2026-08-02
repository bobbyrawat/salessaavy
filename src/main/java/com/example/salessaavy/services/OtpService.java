package com.example.salessaavy.services;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.salessaavy.entity.Otp;
import com.example.salessaavy.repository.OtpRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository otpRepository;
    private final JavaMailSender mailSender;

    private final SecureRandom random = new SecureRandom();

  public void sendOtp(String email) {

    String otp = String.valueOf(100000 + random.nextInt(900000));

    Otp otpEntity = otpRepository.findByEmail(email)
            .orElse(new Otp());

    otpEntity.setEmail(email);
    otpEntity.setOtp(otp);
    otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5));

    otpRepository.save(otpEntity);

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(email);
    message.setSubject("SalesSaavy OTP Verification");
    message.setText("Your OTP is: " + otp + "\n\nThis OTP is valid for 5 minutes.");

    try {
        mailSender.send(message);
        System.out.println("EMAIL SENT SUCCESSFULLY");
    } catch (Exception e) {
        System.out.println("EMAIL FAILED");
        e.printStackTrace();
    }
}

    

    public boolean verifyOtp(String email, String otp) {

    Otp savedOtp = otpRepository.findByEmailAndOtp(email, otp)
            .orElse(null);

    if (savedOtp == null) {
        return false;
    }

    if (savedOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
        otpRepository.delete(savedOtp);
        return false;
    }

    otpRepository.delete(savedOtp);

    return true;
}
}