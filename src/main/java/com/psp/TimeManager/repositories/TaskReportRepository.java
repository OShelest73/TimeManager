package com.psp.TimeManager.repositories;

import com.psp.TimeManager.models.Task;
import com.psp.TimeManager.models.TaskReport;
import com.psp.TimeManager.models.User;
import com.psp.TimeManager.models.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskReportRepository extends JpaRepository<TaskReport, Integer> {
    Optional<TaskReport> findTaskReportById(int id);

    Optional<List<TaskReport>> findTaskReportsByUser(User user);
}
