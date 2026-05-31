package com.marcus.taskmanager.controller;

import com.marcus.taskmanager.dto.TaskRequest;
import com.marcus.taskmanager.dto.TaskResponse;
import com.marcus.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks(Authentication authentication) {
        String owner = authentication.getName();
        return ResponseEntity.ok(taskService.getAllTasks(owner));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable Long id,
            Authentication authentication) {
        String owner = authentication.getName();
        return ResponseEntity.ok(taskService.getTaskById(id, owner));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest request,
            Authentication authentication) {
        String owner = authentication.getName();
        TaskResponse response = taskService.createTask(request, owner);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request,
            Authentication authentication) {
        String owner = authentication.getName();
        return ResponseEntity.ok(taskService.updateTask(id, request, owner));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id,
            Authentication authentication) {
        String owner = authentication.getName();
        taskService.deleteTask(id, owner);
        return ResponseEntity.noContent().build();
    }
}
