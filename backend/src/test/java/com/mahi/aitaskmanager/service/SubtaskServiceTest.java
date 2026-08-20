package com.mahi.aitaskmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.mahi.aitaskmanager.dto.SubtaskRequest;
import com.mahi.aitaskmanager.dto.SubtaskResponse;
import com.mahi.aitaskmanager.dto.SubtaskStatusUpdateRequest;
import com.mahi.aitaskmanager.entity.Subtask;
import com.mahi.aitaskmanager.entity.Task;
import com.mahi.aitaskmanager.enums.SubtaskStatus;
import com.mahi.aitaskmanager.enums.TaskPriority;
import com.mahi.aitaskmanager.enums.TaskStatus;
import com.mahi.aitaskmanager.exception.TaskNotFoundException;
import com.mahi.aitaskmanager.repository.SubtaskRepository;
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

class SubtaskServiceTest {

    @Mock
    private SubtaskRepository subtaskRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private SubtaskService subtaskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateSubtask() {
        Task task = createTask();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(subtaskRepository.findByTaskIdOrderByOrdAsc(1L)).thenReturn(List.of());

        SubtaskRequest request = new SubtaskRequest();
        request.setTitle("Investigate bug");
        request.setDescription("Find root cause");
        request.setStatus(SubtaskStatus.TODO);

        Subtask saved = new Subtask();
        saved.setId(11L);
        saved.setTask(task);
        saved.setTitle(request.getTitle());
        saved.setDescription(request.getDescription());
        saved.setStatus(request.getStatus());
        saved.setOrd(0);
        saved.setCreatedAt(LocalDateTime.now());
        saved.setUpdatedAt(LocalDateTime.now());
        when(subtaskRepository.save(any(Subtask.class))).thenReturn(saved);

        SubtaskResponse response = subtaskService.createSubtask(1L, request);

        assertEquals("Investigate bug", response.getTitle());
        assertEquals(SubtaskStatus.TODO, response.getStatus());
    }

    @Test
    void shouldGetSubtasksMappedByTask() {
        Task task = createTask();
        when(taskRepository.findById(2L)).thenReturn(Optional.of(task));

        Subtask subtask = new Subtask();
        subtask.setId(22L);
        subtask.setTask(task);
        subtask.setTitle("Design solution");
        subtask.setDescription("Draft plan");
        subtask.setStatus(SubtaskStatus.IN_PROGRESS);
        subtask.setOrd(0);
        subtask.setCreatedAt(LocalDateTime.now());
        subtask.setUpdatedAt(LocalDateTime.now());

        when(subtaskRepository.findByTaskIdOrderByOrdAsc(2L)).thenReturn(List.of(subtask));

        List<SubtaskResponse> response = subtaskService.getSubtasks(2L);

        assertEquals(1, response.size());
        assertEquals("Design solution", response.get(0).getTitle());
    }

    @Test
    void shouldUpdateSubtaskStatus() {
        Task task = createTask();
        Subtask subtask = new Subtask();
        subtask.setId(33L);
        subtask.setTask(task);
        subtask.setTitle("Verify tests");
        subtask.setDescription("Run test suite");
        subtask.setStatus(SubtaskStatus.TODO);
        subtask.setOrd(1);
        subtask.setCreatedAt(LocalDateTime.now());
        subtask.setUpdatedAt(LocalDateTime.now());

        when(subtaskRepository.findById(33L)).thenReturn(Optional.of(subtask));
        when(subtaskRepository.save(any(Subtask.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SubtaskStatusUpdateRequest request = new SubtaskStatusUpdateRequest();
        request.setStatus(SubtaskStatus.COMPLETED);

        SubtaskResponse response = subtaskService.updateSubtaskStatus(1L, 33L, request);

        assertEquals(SubtaskStatus.COMPLETED, response.getStatus());
    }

    @Test
    void shouldThrowWhenTaskNotFound() {
        when(taskRepository.findById(404L)).thenReturn(Optional.empty());

        SubtaskRequest request = new SubtaskRequest();
        request.setTitle("Missing");
        request.setStatus(SubtaskStatus.TODO);

        assertThrows(TaskNotFoundException.class, () -> subtaskService.createSubtask(404L, request));
    }

    private Task createTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Main task");
        task.setDescription("Description");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.MEDIUM);
        task.setDueDate(LocalDate.now().plusDays(2));
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        return task;
    }
}
