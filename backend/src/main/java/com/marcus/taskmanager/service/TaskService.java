package com.marcus.taskmanager.service;

import com.marcus.taskmanager.dto.TaskRequest;
import com.marcus.taskmanager.dto.TaskResponse;
import com.marcus.taskmanager.exception.ResourceNotFoundException;
import com.marcus.taskmanager.model.Task;
import com.marcus.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<TaskResponse> getAllTasks(String owner) {
        return taskRepository.findByOwnerOrderByCreatedAtDesc(owner).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse getTaskById(Long id, String owner) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        if (!task.getOwner().equals(owner)) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }

        return toResponse(task);
    }

    @Transactional
    public TaskResponse createTask(TaskRequest request, String owner) {
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus() != null ? request.getStatus() : com.marcus.taskmanager.model.TaskStatus.TODO)
                .priority(request.getPriority() != null ? request.getPriority() : com.marcus.taskmanager.model.TaskPriority.MEDIUM)
                .dueDate(request.getDueDate())
                .owner(owner)
                .build();

        Task saved = taskRepository.save(task);
        return toResponse(saved);
    }

    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest request, String owner) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        if (!task.getOwner().equals(owner)) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }
        task.setDueDate(request.getDueDate());

        Task saved = taskRepository.save(task);
        return toResponse(saved);
    }

    @Transactional
    public void deleteTask(Long id, String owner) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        if (!task.getOwner().equals(owner)) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }

        taskRepository.delete(task);
    }

    private TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .owner(task.getOwner())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}
