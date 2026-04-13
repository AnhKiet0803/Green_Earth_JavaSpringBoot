package com.example.demo.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * HTML home page + status JSON (independent of static resource order).
 */
@RestController
public class RootController {

    private static final String HOME_HTML = loadHomeHtml();

    private static String loadHomeHtml() {
        try {
            var res = new ClassPathResource("home-page.html");
            if (!res.exists()) {
                return "<!DOCTYPE html><html><head><meta charset='utf-8'></head><body><h1>Green Earth API</h1><p>Missing home-page.html on classpath.</p></body></html>";
            }
            try (var in = res.getInputStream()) {
                return StreamUtils.copyToString(in, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            return "<!DOCTYPE html><html><body><p>Error reading home-page.html</p></body></html>";
        }
    }

    @GetMapping(value = {"/", "/index.html"}, produces = "text/html;charset=UTF-8")
    public String home() {
        return HOME_HTML;
    }

    @GetMapping(value = "/api/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> status() {
        return Map.of(
                "ok", true,
                "service", "green-earth-api",
                "hint", "React UI: run npm run dev → usually http://localhost:5173");
    }
}
