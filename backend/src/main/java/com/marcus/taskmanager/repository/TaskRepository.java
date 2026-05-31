package com.marcus.taskmanager.repository;

import com.marcus.taskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByOwnerOrderByCreatedAtDesc(String owner);
    List<Task> findByOwnerAndStatusOrderByCreatedAtDesc(String owner, String status);
    List<Task> findByOwnerAndPriorityOrderByCreatedAtDesc(String owner, String priority);
}
