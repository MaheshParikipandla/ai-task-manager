package com.mahi.aitaskmanager.service;

import com.mahi.aitaskmanager.dto.TaskRequest;
import com.mahi.aitaskmanager.dto.TaskResponse;
import com.mahi.aitaskmanager.dto.TaskStatusUpdateRequest;
import com.mahi.aitaskmanager.entity.Task;
import com.mahi.aitaskmanager.enums.TaskPriority;
import com.mahi.aitaskmanager.enums.TaskStatus;
import com.mahi.aitaskmanager.exception.TaskNotFoundException;
import com.mahi.aitaskmanager.repository.TaskRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateTask() {
        TaskRequest request = new TaskRequest();
        request.setTitle("Test task");
        request.setDescription("Description");
        request.setStatus(TaskStatus.TODO);
        request.setPriority(TaskPriority.MEDIUM);
        request.setDueDate(LocalDate.of(2026, 12, 31));

        Task saved = new Task();
        saved.setId(1L);
        saved.setTitle(request.getTitle());
        saved.setDescription(request.getDescription());
        saved.setStatus(request.getStatus());
        saved.setPriority(request.getPriority());
        saved.setDueDate(request.getDueDate());
        saved.setCreatedAt(LocalDateTime.now());
        saved.setUpdatedAt(LocalDateTime.now());

        when(taskRepository.save(any(Task.class))).thenReturn(saved);

        TaskResponse response = taskService.createTask(request);

        assertEquals(1L, response.getId());
        assertEquals("Test task", response.getTitle());
        assertEquals(TaskStatus.TODO, response.getStatus());
    }

    @Test
    void shouldReturnTasksWithStatusFilter() {
        Task task = createTaskEntity(2L, TaskStatus.COMPLETED, TaskPriority.HIGH);
        when(taskRepository.findByStatus(TaskStatus.COMPLETED)).thenReturn(List.of(task));

        List<TaskResponse> responses = taskService.getTasks(Optional.of(TaskStatus.COMPLETED), Optional.empty());

        assertEquals(1, responses.size());
        assertEquals(TaskStatus.COMPLETED, responses.get(0).getStatus());
    }

    @Test
    void shouldReturnTasksWithPriorityFilter() {
        Task task = createTaskEntity(3L, TaskStatus.TODO, TaskPriority.LOW);
        when(taskRepository.findByPriority(TaskPriority.LOW)).thenReturn(List.of(task));

        List<TaskResponse> responses = taskService.getTasks(Optional.empty(), Optional.of(TaskPriority.LOW));

        assertEquals(1, responses.size());
        assertEquals(TaskPriority.LOW, responses.get(0).getPriority());
    }

    @Test
    void shouldReturnTasksWithStatusAndPriorityFilter() {
        Task task = createTaskEntity(4L, TaskStatus.IN_PROGRESS, TaskPriority.MEDIUM);
        when(taskRepository.findByStatusAndPriority(TaskStatus.IN_PROGRESS, TaskPriority.MEDIUM))
                .thenReturn(List.of(task));

        List<TaskResponse> responses = taskService.getTasks(Optional.of(TaskStatus.IN_PROGRESS), Optional.of(TaskPriority.MEDIUM));

        assertEquals(1, responses.size());
        assertEquals(TaskStatus.IN_PROGRESS, responses.get(0).getStatus());
        assertEquals(TaskPriority.MEDIUM, responses.get(0).getPriority());
    }

    @Test
    void shouldGetTaskById() {
        Task task = createTaskEntity(5L, TaskStatus.TODO, TaskPriority.HIGH);
        when(taskRepository.findById(5L)).thenReturn(Optional.of(task));

        TaskResponse response = taskService.getTaskById(5L);

        assertEquals(5L, response.getId());
        assertEquals("Title 5", response.getTitle());
    }

    @Test
    void shouldThrowWhenTaskNotFound() {
        when(taskRepository.findById(6L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(6L));
    }

    @Test
    void shouldUpdateTask() {
        Task existing = createTaskEntity(7L, TaskStatus.TODO, TaskPriority.LOW);
        when(taskRepository.findById(7L)).thenReturn(Optional.of(existing));

        TaskRequest request = new TaskRequest();
        request.setTitle("Updated title");
        request.setDescription("Updated description");
        request.setStatus(TaskStatus.IN_PROGRESS);
        request.setPriority(TaskPriority.MEDIUM);
        request.setDueDate(LocalDate.of(2026, 11, 15));

        TaskResponse response = taskService.updateTask(7L, request);

        assertEquals("Updated title", response.getTitle());
        assertEquals(TaskStatus.IN_PROGRESS, response.getStatus());
        assertEquals(TaskPriority.MEDIUM, response.getPriority());
    }

    @Test
    void shouldUpdateTaskStatus() {
        Task existing = createTaskEntity(8L, TaskStatus.TODO, TaskPriority.HIGH);
        when(taskRepository.findById(8L)).thenReturn(Optional.of(existing));

        TaskStatusUpdateRequest request = new TaskStatusUpdateRequest();
        request.setStatus(TaskStatus.COMPLETED);

        TaskResponse response = taskService.updateTaskStatus(8L, request);

        assertEquals(TaskStatus.COMPLETED, response.getStatus());
    }

    @Test
    void shouldDeleteTask() {
        when(taskRepository.existsById(9L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(9L);

        taskService.deleteTask(9L);

        verify(taskRepository).deleteById(eq(9L));
    }

    @Test
    void shouldThrowWhenDeleteTaskNotFound() {
        when(taskRepository.existsById(10L)).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(10L));
    }

    private Task createTaskEntity(Long id, TaskStatus status, TaskPriority priority) {
        Task task = new Task();
        task.setId(id);
        task.setTitle("Title " + id);
        task.setDescription("Description for " + id);
        task.setStatus(status);
        task.setPriority(priority);
        task.setDueDate(LocalDate.of(2026, 12, 1));
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        return task;
    }
}
