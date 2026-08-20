package com.mahi.aitaskmanager.mapper;

import com.mahi.aitaskmanager.dto.TaskRequest;
import com.mahi.aitaskmanager.dto.TaskResponse;
import com.mahi.aitaskmanager.entity.Task;
import java.time.LocalDateTime;

public final class TaskMapper {

    private TaskMapper() {
    }

    public static TaskResponse toDto(Task task) {
        if (task == null) {
            return null;
        }

        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setPriority(task.getPriority());
        response.setDueDate(task.getDueDate());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());
        return response;
    }

    public static Task toEntity(TaskRequest request) {
        if (request == null) {
            return null;
        }

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        LocalDateTime now = LocalDateTime.now();
        task.setCreatedAt(now);
        task.setUpdatedAt(now);
        return task;
    }
}
