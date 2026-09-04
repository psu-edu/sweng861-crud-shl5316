package com.sweng861.agiletracker.config;

import com.sweng861.agiletracker.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String FRONTEND_URL =
            "https://sweng861-bucket.s3.us-east-1.amazonaws.com/index.html";

    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/health", "/error", "/oauth2/**", "/login/oauth2/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                .successHandler(successHandler())
            )
            .logout(logout -> logout
                .logoutSuccessUrl(FRONTEND_URL)
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (HttpServletRequest request,
                HttpServletResponse response,
                org.springframework.security.core.Authentication authentication) -> {
            response.sendRedirect(FRONTEND_URL);
        };
    }
}
