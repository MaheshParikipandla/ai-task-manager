package com.mahi.aitaskmanager.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mahi.aitaskmanager.dto.SubtaskRequest;
import com.mahi.aitaskmanager.dto.SubtaskResponse;
import com.mahi.aitaskmanager.enums.SubtaskStatus;
import com.mahi.aitaskmanager.service.SubtaskService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SubtaskController.class)
class SubtaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubtaskService subtaskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldListSubtasks() throws Exception {
        SubtaskResponse response = new SubtaskResponse();
        response.setId(10L);
        response.setTaskId(1L);
        response.setTitle("Investigate bug");
        response.setDescription("Find root cause");
        response.setStatus(SubtaskStatus.TODO);
        response.setOrd(0);
        response.setCreatedAt(LocalDateTime.now());
        response.setUpdatedAt(LocalDateTime.now());

        when(subtaskService.getSubtasks(1L)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/tasks/1/subtasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldCreateSubtask() throws Exception {
        SubtaskRequest request = new SubtaskRequest();
        request.setTitle("Prepare plan");
        request.setDescription("Draft architecture");
        request.setStatus(SubtaskStatus.TODO);

        SubtaskResponse response = new SubtaskResponse();
        response.setId(20L);
        response.setTaskId(1L);
        response.setTitle("Prepare plan");
        response.setDescription("Draft architecture");
        response.setStatus(SubtaskStatus.TODO);
        response.setOrd(0);
        response.setCreatedAt(LocalDateTime.now());
        response.setUpdatedAt(LocalDateTime.now());

        when(subtaskService.createSubtask(1L, request)).thenReturn(response);

        mockMvc.perform(post("/api/tasks/1/subtasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}
