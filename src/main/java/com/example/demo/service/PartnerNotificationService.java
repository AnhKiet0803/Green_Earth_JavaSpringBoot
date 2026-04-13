package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartnerNotificationService {
    private final JavaMailSender mailSender;

    @Value("${app.partner.portal-login-url:http://localhost:3001/partner-login}")
    private String partnerLoginUrl;

    @Value("${spring.mail.username:no-reply@greenearth.local}")
    private String defaultFrom;

    @Value("${app.partner.admin-notify-emails:}")
    private String adminNotifyEmails;

    @Value("${app.partner.admin-review-url:http://localhost:3001/partners-management}")
    private String adminReviewUrl;

    @Value("${app.partner.apply-url:http://localhost:5173/partners#partner-form}")
    private String partnerApplyUrl;

    @PostConstruct
    void warnIfMailNotConfigured() {
        if (defaultFrom == null || defaultFrom.isBlank()) {
            log.warn("spring.mail.username is empty — partner emails will fail. Set application-local.properties or SPRING_MAIL_USERNAME.");
        }
    }

    public void sendNewRequestToAdmins(String organizationName, String contactName, String email, String programType) {
        List<String> recipients = parseAdminEmails(adminNotifyEmails);
        if (recipients.isEmpty()) {
            log.info("Skip admin notification for new partner request because app.partner.admin-notify-emails is empty");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(defaultFrom);
            message.setTo(recipients.toArray(new String[0]));
            message.setSubject("[Green Earth] New Partner Request Submitted");
            message.setText(
                    "A new partner request has been submitted.\n\n" +
                    "Organization: " + organizationName + "\n" +
                    "Contact: " + contactName + "\n" +
                    "Email: " + email + "\n" +
                    "Program: " + programType + "\n\n" +
                    "Review in admin portal: " + adminReviewUrl + "\n"
            );
            mailSender.send(message);
            log.info("Sent admin notification for new partner request: {}", organizationName);
        } catch (Exception e) {
            log.warn("Unable to send admin notification for partner request: {}", organizationName, e);
        }
    }

    public void sendApprovedEmail(String toEmail, String organizationName, String tempPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(defaultFrom);
        message.setTo(toEmail);
        message.setSubject("[Green Earth] Welcome Partner - Registration & Login");
        message.setText(
                "Hello " + organizationName + ",\n\n" +
                "Welcome to Green Earth Partner Network.\n" +
                "Your partnership request has been approved by admin.\n\n" +
                "Please complete your onboarding with the links below:\n" +
                "1) Registration / onboarding form: " + partnerApplyUrl + "\n" +
                "2) Partner login page: " + partnerLoginUrl + "\n\n" +
                "Login credentials:\n" +
                "Login URL: " + partnerLoginUrl + "\n" +
                "Email: " + toEmail + "\n" +
                "Temporary password: " + tempPassword + "\n\n" +
                "After completing registration details, sign in to access Partner Portal.\n" +
                "Security note: please change your password after first login.\n\n" +
                "Best regards,\nGreen Earth Team"
        );
        mailSender.send(message);
        log.info("Sent partner approval email to {}", toEmail);
    }

    public void sendSubmissionReceivedEmail(String toEmail, String organizationName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(defaultFrom);
        message.setTo(toEmail);
        message.setSubject("[Green Earth] Partner Request Received");
        message.setText(
                "Hello " + organizationName + ",\n\n" +
                "We have received your partnership request.\n" +
                "Our admin team will review your submission and send approval details by email.\n\n" +
                "You can review or submit another request at:\n" +
                partnerApplyUrl + "\n\n" +
                "After approval, we will send your partner login link and temporary password.\n\n" +
                "Best regards,\nGreen Earth Team"
        );
        mailSender.send(message);
        log.info("Sent submission confirmation email to {}", toEmail);
    }

    private List<String> parseAdminEmails(String rawEmails) {
        if (rawEmails == null || rawEmails.trim().isEmpty()) {
            return List.of();
        }
        return Arrays.stream(rawEmails.split("[,;]"))
                .map(String::trim)
                .filter(email -> !email.isEmpty())
                .collect(Collectors.toList());
    }
}
