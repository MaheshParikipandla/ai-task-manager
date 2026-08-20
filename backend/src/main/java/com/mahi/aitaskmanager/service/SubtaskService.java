package com.mahi.aitaskmanager.service;

import com.mahi.aitaskmanager.dto.SubtaskRequest;
import com.mahi.aitaskmanager.dto.SubtaskResponse;
import com.mahi.aitaskmanager.dto.SubtaskStatusUpdateRequest;
import com.mahi.aitaskmanager.entity.Subtask;
import com.mahi.aitaskmanager.entity.Task;
import com.mahi.aitaskmanager.enums.SubtaskStatus;
import com.mahi.aitaskmanager.exception.SubtaskNotFoundException;
import com.mahi.aitaskmanager.exception.TaskNotFoundException;
import com.mahi.aitaskmanager.mapper.SubtaskMapper;
import com.mahi.aitaskmanager.repository.SubtaskRepository;
import com.mahi.aitaskmanager.repository.TaskRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubtaskService {

    private final SubtaskRepository subtaskRepository;
    private final TaskRepository taskRepository;

    public SubtaskService(SubtaskRepository subtaskRepository, TaskRepository taskRepository) {
        this.subtaskRepository = subtaskRepository;
        this.taskRepository = taskRepository;
    }

    public List<SubtaskResponse> getSubtasks(Long taskId) {
        Task task = getTaskOrThrow(taskId);
        return subtaskRepository.findByTaskIdOrderByOrdAsc(task.getId()).stream()
                .map(SubtaskMapper::toDto)
                .collect(Collectors.toList());
    }

    public SubtaskResponse createSubtask(Long taskId, SubtaskRequest request) {
        Task task = getTaskOrThrow(taskId);
        List<Subtask> existing = subtaskRepository.findByTaskIdOrderByOrdAsc(taskId);

        Subtask subtask = SubtaskMapper.toEntity(request, task);
        if (request.getOrd() == null) {
            subtask.setOrd(existing.size());
        }
        return SubtaskMapper.toDto(subtaskRepository.save(subtask));
    }

    @Transactional
    public SubtaskResponse updateSubtask(Long taskId, Long subtaskId, SubtaskRequest request) {
        Subtask subtask = getSubtaskForTaskOrThrow(taskId, subtaskId);
        subtask.setTitle(request.getTitle());
        subtask.setDescription(request.getDescription());
        subtask.setStatus(request.getStatus() != null ? request.getStatus() : subtask.getStatus());
        if (request.getOrd() != null) {
            subtask.setOrd(request.getOrd());
        }
        subtask.setUpdatedAt(LocalDateTime.now());
        return SubtaskMapper.toDto(subtaskRepository.save(subtask));
    }

    @Transactional
    public SubtaskResponse updateSubtaskStatus(Long taskId, Long subtaskId, SubtaskStatusUpdateRequest request) {
        Subtask subtask = getSubtaskForTaskOrThrow(taskId, subtaskId);
        subtask.setStatus(request.getStatus() != null ? request.getStatus() : subtask.getStatus());
        subtask.setUpdatedAt(LocalDateTime.now());
        return SubtaskMapper.toDto(subtaskRepository.save(subtask));
    }

    public void deleteSubtask(Long taskId, Long subtaskId) {
        Subtask subtask = getSubtaskForTaskOrThrow(taskId, subtaskId);
        subtaskRepository.delete(subtask);
    }

    private Task getTaskOrThrow(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    private Subtask getSubtaskForTaskOrThrow(Long taskId, Long subtaskId) {
        Subtask subtask = subtaskRepository.findById(subtaskId)
                .orElseThrow(() -> new SubtaskNotFoundException(taskId, subtaskId));
        if (!subtask.getTask().getId().equals(taskId)) {
            throw new SubtaskNotFoundException(taskId, subtaskId);
        }
        return subtask;
    }
}
