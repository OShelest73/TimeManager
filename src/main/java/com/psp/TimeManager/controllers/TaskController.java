package com.psp.TimeManager.controllers;

import com.psp.TimeManager.dtos.*;
import com.psp.TimeManager.mappers.TaskMapper;
import com.psp.TimeManager.mappers.UserMapper;
import com.psp.TimeManager.models.Task;
import com.psp.TimeManager.models.TaskReport;
import com.psp.TimeManager.models.User;
import com.psp.TimeManager.services.StoryPointService;
import com.psp.TimeManager.services.TaskReportService;
import com.psp.TimeManager.services.TaskService;
import com.psp.TimeManager.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;
    private final TaskMapper taskMapper;
    private final UserMapper userMapper;
    private final StoryPointService storyPointService;
    private final TaskReportService taskReportService;

    public TaskController(TaskService taskService, UserService userService, TaskMapper taskMapper, UserMapper userMapper,
                          StoryPointService storyPointService, TaskReportService taskReportService)
    {
        this.taskService = taskService;
        this.userService = userService;
        this.taskMapper = taskMapper;
        this.userMapper = userMapper;
        this.storyPointService = storyPointService;
        this.taskReportService = taskReportService;
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

    @GetMapping("/sp/all")
    public ResponseEntity<List<Integer>> getStoryPoints(){
        List<Integer> storyPoints = taskMapper.FromSpList(storyPointService.findAllStoryPoints());
        return new ResponseEntity<>(storyPoints, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<?> addTask(@RequestBody CreateTaskDto taskDto) {
        Task taskForDB = taskService.addTask(taskDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/appointedTasks/{email}")
    public ResponseEntity<List<TaskDetailsDto>> appointedTasks(@PathVariable String email){
        List<Task> tasks = taskService.findAllTasks();
        List<TaskDetailsDto> output = new ArrayList<>();

        User user = userService.findUserByEmail(email);

        for(Task task: tasks)
        {
            List<User> users = task.getAppointedUsers();
            for(User iterateUser: users)
            {
                if(iterateUser.getId() == user.getId())
                {
                    output.add(taskMapper.toTaskDetails(task));
                }
            }
        }

        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @GetMapping("/tasksToCheck/{email}")
    public ResponseEntity<List<TaskDetailsDto>> tasksToCheck(@PathVariable String email){
        List<Task> tasks = taskService.findAllTasks();
        List<TaskDetailsDto> output = new ArrayList<>();

        User user = userService.findUserByEmail(email);

        for(Task task: tasks)
        {
            if (task.isToCheck() == true && task.getAuthor().getId() == user.getId())
            {
                output.add(taskMapper.toTaskDetails(task));
            }
        }

        return new ResponseEntity<>(output, HttpStatus.OK);
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

    @PostMapping("/notifyCompletion")
    public ResponseEntity<?> notifyCompletion(@RequestBody Map<String, Integer> data){
        int id = data.get("id");

        Task task = taskService.findTaskById(id);
        task.setToCheck(true);
        taskService.updateTask(task);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/decline")
    public ResponseEntity<?> decline(@RequestBody Map<String, Integer> data){
        int id = data.get("id");
        Task task = taskService.findTaskById(id);
        task.setToCheck(false);
        task.setIsAccepted(false);

        List<User> appointedUsers = task.getAppointedUsers();

        for(User user: appointedUsers)
        {
            TaskReport report = new TaskReport();
            report.setTask(task);
            report.setUser(user);
            report.setTeamMembersAmount(appointedUsers.size());

            taskReportService.add(report);
        }

        task.setAppointedUsers(null);
        taskService.updateTask(task);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/accept")
    public ResponseEntity<?> accept(@RequestBody Map<String, Integer> data){
        int id = data.get("id");
        Task task = taskService.findTaskById(id);
        task.setToCheck(false);
        task.setIsAccepted(false);

        List<User> appointedUsers = task.getAppointedUsers();

        for(User user: appointedUsers)
        {
            TaskReport report = new TaskReport();
            report.setTask(task);
            report.setUser(user);
            report.setTeamMembersAmount(appointedUsers.size());

            taskReportService.add(report);
        }

        task.setAppointedUsers(null);
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
