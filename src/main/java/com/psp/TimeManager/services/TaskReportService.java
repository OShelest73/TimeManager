package com.psp.TimeManager.services;

import com.psp.TimeManager.exceptions.UserNotFoundException;
import com.psp.TimeManager.models.Task;
import com.psp.TimeManager.models.TaskReport;
import com.psp.TimeManager.models.User;
import com.psp.TimeManager.repositories.TaskReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskReportService {
    private final TaskReportRepository taskReportRepository;

    public TaskReportService(TaskReportRepository taskReportRepository) {
        this.taskReportRepository = taskReportRepository;
    }

    public TaskReport add(TaskReport report){
        return taskReportRepository.save(report);
    }

    public List<TaskReport> findReports(User user){
        return taskReportRepository.findTaskReportsByUser(user).
                orElseThrow(() -> new UserNotFoundException("Task reports was not found"));
    }
}
