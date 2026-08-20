package com.mahi.aitaskmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class AiServiceTest {

    @Test
    void mockSuggestWhenNoApiKey() {
        AiService s = new AiService("", "");
        List<String> out = s.suggestSubtasks("Implement authentication", "Add login and registration");
        assertEquals(3, out.size());
        // deterministic first element contains the title
        assertEquals(true, out.get(0).contains("Implement authentication"));
    }

    @Test
    void mockSuggestWhenTitleHasParts() {
        AiService s = new AiService("", "");
        List<String> out = s.suggestSubtasks("Design - Backend - Tests", "desc");
        // Should split on '-' into 3 parts
        assertEquals(3, out.size());
    }
}
