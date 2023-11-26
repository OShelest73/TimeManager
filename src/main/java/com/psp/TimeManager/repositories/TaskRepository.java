package com.psp.TimeManager.repositories;

import com.psp.TimeManager.models.Task;
import com.psp.TimeManager.models.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    void deleteTaskById(int id);

    Optional<Task> findTaskById(int id);

    Optional<Task> findTaskByWorkspace(Workspace workspace);
}
