package com.mahi.aitaskmanager.repository;

import com.mahi.aitaskmanager.entity.Subtask;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubtaskRepository extends JpaRepository<Subtask, Long> {
    List<Subtask> findByTaskIdOrderByOrdAsc(Long taskId);
}
