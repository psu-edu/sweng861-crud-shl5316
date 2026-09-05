package com.sweng861.agiletracker.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void unauthenticatedRequestIsRejected() throws Exception {
        // Simulate a client making an HTTP GET request to our endpoint
        mockMvc.perform(get("/api/hello"))

                // Expect Spring Security to block and return 401 Unauthorized
                .andExpect(status().isUnauthorized());
    }

    @Test
    void authenticatedRequestSucceeds() throws Exception {
        // Make a request, but attach a mock Google login session
        mockMvc.perform(get("/api/hello")
                        .with(oauth2Login().attributes(attrs -> attrs.put("email", "mock@example.com"))))
                .andExpect(status().isOk())
                // Expect the response to contain the email we set in the mock
                .andExpect(jsonPath("$.message").value("Hello, mock@example.com!"));
    }
}
