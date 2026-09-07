package com.fitnesusp.usp_gym.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {


    private final JavaMailSender mailSender;


    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    // ==========================================
    // SEND SIMPLE EMAIL
    // ==========================================

    public void sendEmail(
            String to,
            String subject,
            String message) {


        SimpleMailMessage email =
                new SimpleMailMessage();


        email.setTo(to);

        email.setSubject(subject);

        email.setText(message);


        mailSender.send(email);
    }
}