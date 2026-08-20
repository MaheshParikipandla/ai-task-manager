package com.mahi.aitaskmanager.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.List;

import com.mahi.aitaskmanager.dto.TaskResponse;
import com.mahi.aitaskmanager.service.AiService;
import com.mahi.aitaskmanager.service.TaskService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AiController.class)
public class AiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private AiService aiService;

    @Test
    void breakdownReturnsSuggestions() throws Exception {
        TaskResponse task = new TaskResponse();
        task.setId(1L);
        task.setTitle("New feature");
        task.setDescription("Do stuff");

        when(taskService.getTaskById(1L)).thenReturn(task);
        when(aiService.suggestSubtasks(task.getTitle(), task.getDescription()))
                .thenReturn(List.of("step1", "step2"));

        mockMvc.perform(post("/api/tasks/1/ai/breakdown").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"step1\",\"step2\"]"));
    }
}
