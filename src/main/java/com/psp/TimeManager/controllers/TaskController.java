package com.psp.TimeManager.controllers;

import com.psp.TimeManager.dtos.CreateTaskDto;
import com.psp.TimeManager.dtos.TaskDetailsDto;
import com.psp.TimeManager.mappers.TaskMapper;
import com.psp.TimeManager.models.Task;
import com.psp.TimeManager.services.TaskService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper)
    {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @GetMapping("/all") //TODO в теории можно сносить
    public ResponseEntity<List<Task>> getWorkspaceTasks()
    {
        List<Task> tasks = taskService.findAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<TaskDetailsDto> getTask(@PathVariable("id") int id)
    {
        TaskDetailsDto task = taskMapper.toTaskDetails(taskService.findTaskById(id));
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<?> addTask(@RequestBody CreateTaskDto taskDto) {
        Task taskForDB = taskService.addTask(taskDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateTask(@RequestBody Task task) {
        Task updateTask = taskService.updateTask(task);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") int id) {
        Task task = taskService.findTaskById(id);

        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
