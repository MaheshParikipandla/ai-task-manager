package com.mahi.aitaskmanager.repository;

import com.mahi.aitaskmanager.entity.Task;
import com.mahi.aitaskmanager.enums.TaskPriority;
import com.mahi.aitaskmanager.enums.TaskStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByPriority(TaskPriority priority);

    List<Task> findByStatusAndPriority(TaskStatus status, TaskPriority priority);
}
