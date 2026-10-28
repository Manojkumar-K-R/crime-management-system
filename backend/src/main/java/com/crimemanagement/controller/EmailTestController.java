package com.crimemanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crimemanagement.service.EmailService;

@RestController
public class EmailTestController {

    private final EmailService emailService;

    public EmailTestController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/api/test-email")
    public String sendTestEmail(@RequestParam String to) {

        emailService.sendEmail(
                to,
                "Crime Management System - Test Email",
                "Congratulations! Your email configuration is working successfully.");

        return "Email sent successfully.";
    }
}