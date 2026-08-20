package com.mahi.aitaskmanager.controller;

import java.util.List;

import com.mahi.aitaskmanager.service.AiService;
import com.mahi.aitaskmanager.service.TaskService;
import com.mahi.aitaskmanager.dto.TaskResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks/{id}/ai")
public class AiController {

    private final TaskService taskService;
    private final AiService aiService;

    public AiController(TaskService taskService, AiService aiService) {
        this.taskService = taskService;
        this.aiService = aiService;
    }

    @PostMapping("/breakdown")
    public ResponseEntity<?> breakdownTask(@PathVariable Long id) {
        TaskResponse task = taskService.getTaskById(id);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        List<String> suggestions = aiService.suggestSubtasks(task.getTitle(), task.getDescription());
        return ResponseEntity.ok(suggestions);
    }
}
