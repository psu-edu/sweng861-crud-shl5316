package com.sweng861.agiletracker.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/user")
    public Map<String, Object> currentUser(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return Map.of("authenticated", false);
        }

        return Map.of(
            "authenticated", true,
            "name", principal.getAttribute("name"),
            "email", principal.getAttribute("email"),
            "givenName", principal.getAttribute("given_name"),
            "familyName", principal.getAttribute("family_name"),
            "picture", principal.getAttribute("picture"),
            "attributes", principal.getAttributes()
        );
    }
}
