package com.marcus.taskmanager.service;

import com.marcus.taskmanager.dto.TaskRequest;
import com.marcus.taskmanager.dto.TaskResponse;
import com.marcus.taskmanager.exception.ResourceNotFoundException;
import com.marcus.taskmanager.model.Task;
import com.marcus.taskmanager.model.TaskPriority;
import com.marcus.taskmanager.model.TaskStatus;
import com.marcus.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private TaskRequest taskRequest;
    private final String owner = "testuser";

    @BeforeEach
    void setUp() {
        task = Task.builder()
                .id(1L)
                .title("Test Task")
                .description("Test Description")
                .status(TaskStatus.TODO)
                .priority(TaskPriority.MEDIUM)
                .dueDate(LocalDateTime.now().plusDays(7))
                .owner(owner)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        taskRequest = new TaskRequest();
        taskRequest.setTitle("New Task");
        taskRequest.setDescription("New Description");
        taskRequest.setStatus(TaskStatus.IN_PROGRESS);
        taskRequest.setPriority(TaskPriority.HIGH);
        taskRequest.setDueDate(LocalDateTime.now().plusDays(3));
    }

    @Test
    void getAllTasks_ShouldReturnListOfTasks() {
        when(taskRepository.findByOwnerOrderByCreatedAtDesc(owner))
                .thenReturn(List.of(task));

        List<TaskResponse> result = taskService.getAllTasks(owner);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Task");
        assertThat(result.get(0).getOwner()).isEqualTo(owner);
        verify(taskRepository).findByOwnerOrderByCreatedAtDesc(owner);
    }

    @Test
    void getTaskById_ShouldReturnTask_WhenOwnerMatches() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskResponse result = taskService.getTaskById(1L, owner);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Task");
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getTaskById_ShouldThrowException_WhenNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskById(99L, owner))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Task not found");
    }

    @Test
    void getTaskById_ShouldThrowException_WhenOwnerDoesNotMatch() {
        Task otherTask = Task.builder()
                .id(2L)
                .title("Other Task")
                .owner("otheruser")
                .status(TaskStatus.TODO)
                .priority(TaskPriority.LOW)
                .build();
        when(taskRepository.findById(2L)).thenReturn(Optional.of(otherTask));

        assertThatThrownBy(() -> taskService.getTaskById(2L, owner))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Task not found");
    }

    @Test
    void createTask_ShouldReturnCreatedTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponse result = taskService.createTask(taskRequest, owner);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Task");
        assertThat(result.getOwner()).isEqualTo(owner);

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(captor.capture());
        Task saved = captor.getValue();
        assertThat(saved.getTitle()).isEqualTo("New Task");
        assertThat(saved.getOwner()).isEqualTo(owner);
    }

    @Test
    void createTask_ShouldUseDefaults_WhenStatusAndPriorityNull() {
        TaskRequest minimalRequest = new TaskRequest();
        minimalRequest.setTitle("Minimal Task");

        Task savedMinimal = Task.builder()
                .id(2L)
                .title("Minimal Task")
                .status(TaskStatus.TODO)
                .priority(TaskPriority.MEDIUM)
                .owner(owner)
                .build();

        when(taskRepository.save(any(Task.class))).thenReturn(savedMinimal);

        TaskResponse result = taskService.createTask(minimalRequest, owner);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Minimal Task");

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(TaskStatus.TODO);
        assertThat(captor.getValue().getPriority()).isEqualTo(TaskPriority.MEDIUM);
    }

    @Test
    void updateTask_ShouldReturnUpdatedTask() {
        TaskRequest updateRequest = new TaskRequest();
        updateRequest.setTitle("Updated Title");
        updateRequest.setDescription("Updated Description");
        updateRequest.setStatus(TaskStatus.DONE);

        Task updatedTask = Task.builder()
                .id(1L)
                .title("Updated Title")
                .description("Updated Description")
                .status(TaskStatus.DONE)
                .priority(TaskPriority.MEDIUM)
                .owner(owner)
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        TaskResponse result = taskService.updateTask(1L, updateRequest, owner);

        assertThat(result.getTitle()).isEqualTo("Updated Title");
        assertThat(result.getStatus()).isEqualTo(TaskStatus.DONE);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void updateTask_ShouldThrowException_WhenNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateTask(99L, taskRequest, owner))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Task not found");
    }

    @Test
    void deleteTask_ShouldDeleteTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L, owner);

        verify(taskRepository).delete(task);
    }

    @Test
    void deleteTask_ShouldThrowException_WhenNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.deleteTask(99L, owner))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Task not found");
    }

    @Test
    void deleteTask_ShouldThrowException_WhenOwnerDoesNotMatch() {
        Task otherTask = Task.builder()
                .id(2L)
                .title("Other Task")
                .owner("otheruser")
                .status(TaskStatus.TODO)
                .priority(TaskPriority.LOW)
                .build();
        when(taskRepository.findById(2L)).thenReturn(Optional.of(otherTask));

        assertThatThrownBy(() -> taskService.deleteTask(2L, owner))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Task not found");
    }
}
