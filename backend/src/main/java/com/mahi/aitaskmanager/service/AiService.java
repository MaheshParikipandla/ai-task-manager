package com.mahi.aitaskmanager.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
public class AiService {

    private final String apiKey;
    private final String baseUrl;

    public AiService(@Value("${AI_API_KEY:}") String apiKey,
                     @Value("${AI_BASE_URL:}") String baseUrl) {
        this.apiKey = apiKey == null ? "" : apiKey.trim();
        this.baseUrl = baseUrl == null ? "" : baseUrl.trim();
    }

    /**
     * Suggest a list of subtasks for the given task title/description.
     * If AI is not configured (no API key), returns a simple deterministic mock list
     * so frontend and integration tests can be developed without an API key.
     */
    public List<String> suggestSubtasks(String title, String description) {
        if (apiKey.isEmpty() || baseUrl.isEmpty()) {
            return mockSuggest(title, description);
        }

        // Placeholder for a real AI call. Keep this method small and focused so
        // the integration can be added later without changing callers.
        // For now, still return a mock until real integration is implemented.
        return mockSuggest(title, description);
    }

    private List<String> mockSuggest(String title, String description) {
        List<String> result = new ArrayList<>();
        if (title == null || title.isBlank()) {
            result.add("Define the task requirements.");
            result.add("Estimate effort and time.");
            result.add("Break down into implementation steps.");
            return result;
        }

        // Simple heuristic: split title words to create subtasks — deterministic and safe
        String[] parts = title.split("[:\\-–—]");
        if (parts.length > 1) {
            for (String p : parts) {
                String s = p.trim();
                if (!s.isEmpty()) {
                    result.add("Work on: " + s);
                }
            }
        } else {
            // If single short title, return 3 suggested subtasks
            result.add("Research and gather requirements for '" + title + "'.");
            result.add("Implement core logic for '" + title + "'.");
            result.add("Write tests and documentation for '" + title + "'.");
        }
        return result;
    }
}
