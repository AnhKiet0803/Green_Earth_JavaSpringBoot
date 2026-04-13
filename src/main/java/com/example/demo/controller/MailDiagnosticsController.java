package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Quick check that Spring loaded Gmail + app password (password never exposed).
 * GET http://localhost:8080/api/green_earth/dev/mail-config
 * POST http://localhost:8080/api/green_earth/dev/mail-send-test?to=you@gmail.com (only when app.dev.partner-reset-enabled=true)
 */
@RestController
@RequestMapping("/api/green_earth/dev")
@CrossOrigin(origins = "*")
public class MailDiagnosticsController {

    private final Environment environment;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String mailFrom;

    public MailDiagnosticsController(Environment environment, JavaMailSender mailSender) {
        this.environment = environment;
        this.mailSender = mailSender;
    }

    @GetMapping("/mail-config")
    public ResponseEntity<Map<String, Object>> mailConfig() {
        String user = environment.getProperty("spring.mail.username", "");
        String pass = environment.getProperty("spring.mail.password", "");
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("smtpHost", environment.getProperty("spring.mail.host", ""));
        body.put("smtpPort", environment.getProperty("spring.mail.port", ""));
        body.put("usernameConfigured", user != null && !user.isBlank());
        body.put("passwordConfigured", pass != null && !pass.isBlank());
        body.put("usernameHint", maskEmail(user));
        body.put(
                "hint",
                "If username/password = false: run mvn compile or mvn spring-boot:run so application-local.properties is copied to target/classes, then restart the backend."
        );
        return ResponseEntity.ok(body);
    }

    /**
     * Sends a minimal test email. On SMTP failure the response body includes the error message.
     */
    @PostMapping("/mail-send-test")
    public ResponseEntity<Map<String, Object>> mailSendTest(@RequestParam String to) {
        Boolean devOn = environment.getProperty("app.dev.partner-reset-enabled", Boolean.class, false);
        if (!Boolean.TRUE.equals(devOn)) {
            Map<String, Object> denied = new LinkedHashMap<>();
            denied.put("ok", false);
            denied.put("error", "mail-send-test disabled (set app.dev.partner-reset-enabled=true for local demo)");
            return ResponseEntity.status(403).body(denied);
        }
        if (to == null || to.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("ok", false, "error", "query param 'to' is required"));
        }
        if (mailFrom == null || mailFrom.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "ok", false,
                    "error", "spring.mail.username is empty — check application-local.properties on classpath"
            ));
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailFrom);
            message.setTo(to.trim());
            message.setSubject("[Green Earth] SMTP test");
            message.setText("If you see this message, JavaMailSender + Gmail SMTP work on this machine.");
            mailSender.send(message);
            Map<String, Object> ok = new LinkedHashMap<>();
            ok.put("ok", true);
            ok.put("sentTo", to.trim());
            ok.put("from", maskEmail(mailFrom));
            return ResponseEntity.ok(ok);
        } catch (Exception e) {
            Map<String, Object> err = new LinkedHashMap<>();
            err.put("ok", false);
            err.put("error", e.getMessage());
            err.put("errorType", e.getClass().getSimpleName());
            return ResponseEntity.status(500).body(err);
        }
    }

    private static String maskEmail(String email) {
        if (email == null || email.isBlank()) {
            return "";
        }
        int at = email.indexOf('@');
        if (at < 2) {
            return "***";
        }
        return email.charAt(0) + "***" + email.substring(at);
    }
}
