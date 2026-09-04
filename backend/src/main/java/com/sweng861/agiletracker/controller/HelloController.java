package com.sweng861.agiletracker.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public Map<String, String> hello(@AuthenticationPrincipal OAuth2User principal) {
        String email = principal != null ? principal.getAttribute("email") : "user";
        return Map.of("message", "Hello, " + email + "!");
    }
}
