package com.sweng861.AgileTracker;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class StoryController {

    @GetMapping("/health")
    public Map<String, String> healthCheck() {
        return Map.of("status", "ok");
    }

    @GetMapping("/api/hello")
    public Map<String, String> hello() {
        return Map.of("message", "Hello, World!");
    }

    @GetMapping("/api/stories")
    public Map<String, String> getStories() {
        return Map.of("stories", Collections.emptyList().toString());
    }
}
