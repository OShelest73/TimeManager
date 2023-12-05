package com.psp.TimeManager.controllers;

import com.psp.TimeManager.dtos.TaskReportDto;
import com.psp.TimeManager.mappers.TaskReportMapper;
import com.psp.TimeManager.models.User;
import com.psp.TimeManager.services.TaskReportService;
import com.psp.TimeManager.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/report")
public class TaskReportController {
    private final TaskReportService taskReportService;
    private final UserService userService;
    private final TaskReportMapper taskReportMapper;

    public TaskReportController(TaskReportService taskReportService, UserService userService, TaskReportMapper taskReportMapper) {
        this.taskReportService = taskReportService;
        this.userService = userService;
        this.taskReportMapper = taskReportMapper;
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<List<TaskReportDto>> getReports(@PathVariable int id)
    {
        User user = userService.findUserById(id);
        List<TaskReportDto> reports = taskReportMapper.toListTaskReportDto(taskReportService.findReports(user));

        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

}
