package com.marcus.taskmanager.dto;

import com.marcus.taskmanager.model.TaskPriority;
import com.marcus.taskmanager.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDateTime dueDate;
    private String owner;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
