package com.marcus.taskmanager.controller;

import com.marcus.taskmanager.dto.TaskRequest;
import com.marcus.taskmanager.dto.TaskResponse;
import com.marcus.taskmanager.model.TaskPriority;
import com.marcus.taskmanager.model.TaskStatus;
import com.marcus.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TaskController taskController;

    private TaskResponse taskResponse;
    private TaskRequest taskRequest;
    private final String owner = "testuser";

    @BeforeEach
    void setUp() {
        taskResponse = TaskResponse.builder()
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

        when(authentication.getName()).thenReturn(owner);
    }

    @Test
    void getAllTasks_ShouldReturnListOfTasks() {
        when(taskService.getAllTasks(owner)).thenReturn(List.of(taskResponse));

        ResponseEntity<List<TaskResponse>> response = taskController.getAllTasks(authentication);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getTitle()).isEqualTo("Test Task");
        verify(taskService).getAllTasks(owner);
    }

    @Test
    void getTaskById_ShouldReturnTask() {
        when(taskService.getTaskById(1L, owner)).thenReturn(taskResponse);

        ResponseEntity<TaskResponse> response = taskController.getTaskById(1L, authentication);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Test Task");
        verify(taskService).getTaskById(1L, owner);
    }

    @Test
    void createTask_ShouldReturnCreated() {
        when(taskService.createTask(taskRequest, owner)).thenReturn(taskResponse);

        ResponseEntity<TaskResponse> response = taskController.createTask(taskRequest, authentication);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Test Task");
        verify(taskService).createTask(taskRequest, owner);
    }

    @Test
    void updateTask_ShouldReturnUpdatedTask() {
        TaskResponse updatedResponse = TaskResponse.builder()
                .id(1L)
                .title("Updated Task")
                .description("Updated Description")
                .status(TaskStatus.DONE)
                .priority(TaskPriority.HIGH)
                .owner(owner)
                .build();

        when(taskService.updateTask(1L, taskRequest, owner)).thenReturn(updatedResponse);

        ResponseEntity<TaskResponse> response = taskController.updateTask(1L, taskRequest, authentication);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualTo("Updated Task");
        assertThat(response.getBody().getStatus()).isEqualTo(TaskStatus.DONE);
        verify(taskService).updateTask(1L, taskRequest, owner);
    }

    @Test
    void deleteTask_ShouldReturnNoContent() {
        doNothing().when(taskService).deleteTask(1L, owner);

        ResponseEntity<Void> response = taskController.deleteTask(1L, authentication);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
        verify(taskService).deleteTask(1L, owner);
    }
}
