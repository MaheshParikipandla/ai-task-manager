package com.mahi.aitaskmanager.service;

import com.mahi.aitaskmanager.dto.TaskRequest;
import com.mahi.aitaskmanager.dto.TaskResponse;
import com.mahi.aitaskmanager.dto.TaskStatusUpdateRequest;
import com.mahi.aitaskmanager.entity.Task;
import com.mahi.aitaskmanager.enums.TaskPriority;
import com.mahi.aitaskmanager.enums.TaskStatus;
import com.mahi.aitaskmanager.exception.TaskNotFoundException;
import com.mahi.aitaskmanager.mapper.TaskMapper;
import com.mahi.aitaskmanager.repository.TaskRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponse createTask(TaskRequest request) {
        Task task = TaskMapper.toEntity(request);
        Task savedTask = taskRepository.save(task);
        return TaskMapper.toDto(savedTask);
    }

    public List<TaskResponse> getTasks(Optional<TaskStatus> status, Optional<TaskPriority> priority) {
        List<Task> tasks;

        if (status.isPresent() && priority.isPresent()) {
            tasks = taskRepository.findByStatusAndPriority(status.get(), priority.get());
        } else if (status.isPresent()) {
            tasks = taskRepository.findByStatus(status.get());
        } else if (priority.isPresent()) {
            tasks = taskRepository.findByPriority(priority.get());
        } else {
            tasks = taskRepository.findAll();
        }

        return tasks.stream()
                .map(TaskMapper::toDto)
                .collect(Collectors.toList());
    }

    public TaskResponse getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        return TaskMapper.toDto(task);
    }

    @Transactional
    public TaskResponse updateTask(Long taskId, TaskRequest request) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        existingTask.setTitle(request.getTitle());
        existingTask.setDescription(request.getDescription());
        existingTask.setStatus(request.getStatus());
        existingTask.setPriority(request.getPriority());
        existingTask.setDueDate(request.getDueDate());
        existingTask.setUpdatedAt(LocalDateTime.now());

        return TaskMapper.toDto(existingTask);
    }

    @Transactional
    public TaskResponse updateTaskStatus(Long taskId, TaskStatusUpdateRequest request) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        existingTask.setStatus(request.getStatus());
        existingTask.setUpdatedAt(LocalDateTime.now());

        return TaskMapper.toDto(existingTask);
    }

    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException(taskId);
        }
        taskRepository.deleteById(taskId);
    }
}
