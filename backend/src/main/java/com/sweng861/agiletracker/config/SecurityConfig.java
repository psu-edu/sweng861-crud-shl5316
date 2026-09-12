package com.sweng861.agiletracker.config;

import com.sweng861.agiletracker.security.RateLimitFilter;
import com.sweng861.agiletracker.service.CustomOAuth2UserService;
import com.sweng861.agiletracker.service.CustomOidcUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String frontendUrl;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOidcUserService customOidcUserService;
    private final RateLimitFilter rateLimitFilter;

    public SecurityConfig(@Value("${app.frontend-url:http://localhost:8000}") String frontendUrl,
                          CustomOAuth2UserService customOAuth2UserService,
                          CustomOidcUserService customOidcUserService,
                          RateLimitFilter rateLimitFilter) {
        this.frontendUrl = frontendUrl;
        this.customOAuth2UserService = customOAuth2UserService;
        this.customOidcUserService = customOidcUserService;
        this.rateLimitFilter = rateLimitFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Enforce IP-based rate limiting and log suspicious traffic patterns
            .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
            // Configure cross-origin resource sharing for the S3 frontend
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // Disable CSRF since API is decoupled and relies on CORS and authenticated sessions
            .csrf(csrf -> csrf.disable())
            // Handle unauthenticated API requests with 401 JSON instead of redirects
            .exceptionHandling(ex -> ex.authenticationEntryPoint(restAuthenticationEntryPoint()))
            // Define public vs. protected route access rules
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/health",
                        "/error",
                        "/oauth2/**",
                        "/login/oauth2/**",
                        "/logout",
                        "/api/breeds/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            // Configure OAuth2/OIDC login flow and user synchronization services
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService)
                    .oidcUserService(customOidcUserService)
                )
                .successHandler(successHandler())
            )
            // Configure session invalidation and post-logout redirect
            .logout(logoutConfig());

        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint restAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            String path = request.getRequestURI();
            if (path != null && path.startsWith("/api/")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                String body = "{\"error\":\"Unauthorized\"}";
                response.getWriter().write(body);
                response.getWriter().flush();
            } else {
                // default behavior for non-API requests: redirect to login (allow OAuth flow)
                response.sendRedirect("/oauth2/authorization/google");
            }
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:8000",
                frontendUrl,
                "https://sweng861-bucket.s3.us-east-1.amazonaws.com"
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (HttpServletRequest request,
                HttpServletResponse response,
                org.springframework.security.core.Authentication authentication) -> {
            response.sendRedirect(frontendUrl);
        };
    }

    private @NonNull Customizer<LogoutConfigurer<HttpSecurity>> logoutConfig() {
        return logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl(frontendUrl)
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll();
    }
}
