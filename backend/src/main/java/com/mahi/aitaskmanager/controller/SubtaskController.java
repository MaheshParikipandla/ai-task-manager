package com.mahi.aitaskmanager.controller;

import com.mahi.aitaskmanager.dto.SubtaskRequest;
import com.mahi.aitaskmanager.dto.SubtaskResponse;
import com.mahi.aitaskmanager.dto.SubtaskStatusUpdateRequest;
import com.mahi.aitaskmanager.service.SubtaskService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks/{taskId}/subtasks")
public class SubtaskController {

    private final SubtaskService subtaskService;

    public SubtaskController(SubtaskService subtaskService) {
        this.subtaskService = subtaskService;
    }

    @GetMapping
    public ResponseEntity<List<SubtaskResponse>> getSubtasks(@PathVariable Long taskId) {
        return ResponseEntity.ok(subtaskService.getSubtasks(taskId));
    }

    @PostMapping
    public ResponseEntity<SubtaskResponse> createSubtask(@PathVariable Long taskId,
                                                        @Valid @RequestBody SubtaskRequest request) {
        SubtaskResponse response = subtaskService.createSubtask(taskId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{subtaskId}")
    public ResponseEntity<SubtaskResponse> updateSubtask(@PathVariable Long taskId,
                                                        @PathVariable Long subtaskId,
                                                        @Valid @RequestBody SubtaskRequest request) {
        SubtaskResponse response = subtaskService.updateSubtask(taskId, subtaskId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{subtaskId}/status")
    public ResponseEntity<SubtaskResponse> updateSubtaskStatus(@PathVariable Long taskId,
                                                              @PathVariable Long subtaskId,
                                                              @Valid @RequestBody SubtaskStatusUpdateRequest request) {
        SubtaskResponse response = subtaskService.updateSubtaskStatus(taskId, subtaskId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{subtaskId}")
    public ResponseEntity<Void> deleteSubtask(@PathVariable Long taskId, @PathVariable Long subtaskId) {
        subtaskService.deleteSubtask(taskId, subtaskId);
        return ResponseEntity.noContent().build();
    }
}
