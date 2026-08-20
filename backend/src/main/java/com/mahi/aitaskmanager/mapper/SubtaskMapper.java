package com.mahi.aitaskmanager.mapper;

import com.mahi.aitaskmanager.dto.SubtaskRequest;
import com.mahi.aitaskmanager.dto.SubtaskResponse;
import com.mahi.aitaskmanager.entity.Subtask;
import com.mahi.aitaskmanager.entity.Task;
import com.mahi.aitaskmanager.enums.SubtaskStatus;
import java.time.LocalDateTime;

public final class SubtaskMapper {

    private SubtaskMapper() {
    }

    public static SubtaskResponse toDto(Subtask subtask) {
        if (subtask == null) {
            return null;
        }

        SubtaskResponse response = new SubtaskResponse();
        response.setId(subtask.getId());
        response.setTaskId(subtask.getTask() != null ? subtask.getTask().getId() : null);
        response.setTitle(subtask.getTitle());
        response.setDescription(subtask.getDescription());
        response.setStatus(subtask.getStatus());
        response.setOrd(subtask.getOrd());
        response.setCreatedAt(subtask.getCreatedAt());
        response.setUpdatedAt(subtask.getUpdatedAt());
        return response;
    }

    public static Subtask toEntity(SubtaskRequest request, Task task) {
        if (request == null) {
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        Subtask subtask = new Subtask();
        subtask.setTask(task);
        subtask.setTitle(request.getTitle());
        subtask.setDescription(request.getDescription());
        subtask.setStatus(request.getStatus() != null ? request.getStatus() : SubtaskStatus.TODO);
        subtask.setOrd(request.getOrd() != null ? request.getOrd() : 0);
        subtask.setCreatedAt(now);
        subtask.setUpdatedAt(now);
        return subtask;
    }
}
