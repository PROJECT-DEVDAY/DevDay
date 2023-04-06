package com.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Async
    public void send(String email, String title, String message) {
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setTo(email);
        smm.setSubject(title);
        smm.setText(message);

        javaMailSender.send(smm);
    }

}