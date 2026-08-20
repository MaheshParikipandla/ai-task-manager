package com.mahi.aitaskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mahi.aitaskmanager.dto.TaskRequest;
import com.mahi.aitaskmanager.dto.TaskResponse;
import com.mahi.aitaskmanager.dto.TaskStatusUpdateRequest;
import com.mahi.aitaskmanager.enums.TaskPriority;
import com.mahi.aitaskmanager.enums.TaskStatus;
import com.mahi.aitaskmanager.service.TaskService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @Test
    void shouldCreateTask() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("New task");
        request.setDescription("Description");
        request.setStatus(TaskStatus.TODO);
        request.setPriority(TaskPriority.HIGH);
        request.setDueDate(LocalDate.of(2026, 9, 1));

        TaskResponse response = createResponse(1L, request);
        Mockito.when(taskService.createTask(Mockito.any(TaskRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New task"));
    }

    @Test
    void shouldGetTasks() throws Exception {
        TaskResponse task1 = createResponse(1L, "Task 1", TaskStatus.TODO, TaskPriority.LOW);
        Mockito.when(taskService.getTasks(Mockito.any(), Mockito.any())).thenReturn(List.of(task1));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Task 1"));
    }

    @Test
    void shouldGetTaskById() throws Exception {
        TaskResponse response = createResponse(2L, "Single task", TaskStatus.IN_PROGRESS, TaskPriority.MEDIUM);
        Mockito.when(taskService.getTaskById(2L)).thenReturn(response);

        mockMvc.perform(get("/api/tasks/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void shouldUpdateTask() throws Exception {
        TaskRequest request = new TaskRequest();
        request.setTitle("Updated");
        request.setDescription("Updated description");
        request.setStatus(TaskStatus.IN_PROGRESS);
        request.setPriority(TaskPriority.HIGH);
        request.setDueDate(LocalDate.of(2026, 10, 1));

        TaskResponse response = createResponse(3L, request);
        Mockito.when(taskService.updateTask(Mockito.eq(3L), Mockito.any(TaskRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/tasks/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.title").value("Updated"));
    }

    @Test
    void shouldUpdateTaskStatus() throws Exception {
        TaskStatusUpdateRequest request = new TaskStatusUpdateRequest();
        request.setStatus(TaskStatus.COMPLETED);

        TaskResponse response = createResponse(4L, "Status change", TaskStatus.COMPLETED, TaskPriority.MEDIUM);
        Mockito.when(taskService.updateTaskStatus(Mockito.eq(4L), Mockito.any(TaskStatusUpdateRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/api/tasks/4/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        Mockito.doNothing().when(taskService).deleteTask(5L);

        mockMvc.perform(delete("/api/tasks/5"))
                .andExpect(status().isNoContent());
    }

    private TaskResponse createResponse(Long id, TaskRequest request) {
        TaskResponse response = new TaskResponse();
        response.setId(id);
        response.setTitle(request.getTitle());
        response.setDescription(request.getDescription());
        response.setStatus(request.getStatus());
        response.setPriority(request.getPriority());
        response.setDueDate(request.getDueDate());
        response.setCreatedAt(LocalDateTime.now());
        response.setUpdatedAt(LocalDateTime.now());
        return response;
    }

    private TaskResponse createResponse(Long id, String title, TaskStatus status, TaskPriority priority) {
        TaskResponse response = new TaskResponse();
        response.setId(id);
        response.setTitle(title);
        response.setDescription("Description");
        response.setStatus(status);
        response.setPriority(priority);
        response.setDueDate(LocalDate.of(2026, 11, 11));
        response.setCreatedAt(LocalDateTime.now());
        response.setUpdatedAt(LocalDateTime.now());
        return response;
    }
}
