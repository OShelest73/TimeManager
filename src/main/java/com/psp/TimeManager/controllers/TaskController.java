package com.psp.TimeManager.controllers;

import com.psp.TimeManager.dtos.*;
import com.psp.TimeManager.mappers.TaskMapper;
import com.psp.TimeManager.mappers.UserMapper;
import com.psp.TimeManager.models.Task;
import com.psp.TimeManager.models.User;
import com.psp.TimeManager.models.Workspace;
import com.psp.TimeManager.services.TaskService;
import com.psp.TimeManager.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;
    private final TaskMapper taskMapper;
    private final UserMapper userMapper;

    public TaskController(TaskService taskService, UserService userService, TaskMapper taskMapper, UserMapper userMapper)
    {
        this.taskService = taskService;
        this.userService = userService;
        this.taskMapper = taskMapper;
        this.userMapper = userMapper;
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

    @GetMapping("/appointed/{id}")
    public ResponseEntity<List<UserPreviewDto>> appointedUser(@PathVariable int id){
        Task task = taskService.findTaskById(id);
        List<UserPreviewDto> appointedUsers = userMapper.mapToPreview(task.getAppointedUsers());

        return new ResponseEntity<>(appointedUsers, HttpStatus.OK);
    }

    @PostMapping("/appoint")
    public ResponseEntity<?> appointUser(@RequestBody AppointmentDto appointmentDto){
        Task task = taskService.findTaskById(appointmentDto.taskId());
        List<User> appointedUsers = task.getAppointedUsers();
        User user = userService.findUserById(appointmentDto.userId());
        appointedUsers.add(user);
        task.setAppointedUsers(appointedUsers);
        taskService.updateTask(task);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/user/remove")
    public ResponseEntity<?> removeFromTask(@RequestBody AppointmentDto appointmentDto){
        User user = userService.findUserById(appointmentDto.userId());
        Task task = taskService.findTaskById(appointmentDto.taskId());

        List<User> appointedUsers = task.getAppointedUsers();
        appointedUsers.remove(user);

        task.setAppointedUsers(appointedUsers);
        taskService.updateTask(task);
        return new ResponseEntity<>(HttpStatus.OK);
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
