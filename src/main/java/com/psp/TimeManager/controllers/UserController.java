package com.psp.TimeManager.controllers;

import com.psp.TimeManager.dtos.UserDto;
import com.psp.TimeManager.dtos.UserPreviewDto;
import com.psp.TimeManager.dtos.WorkspaceDto;
import com.psp.TimeManager.mappers.UserMapper;
import com.psp.TimeManager.mappers.WorkspaceMapper;
import com.psp.TimeManager.dtos.InviteDto;
import com.psp.TimeManager.models.Task;
import com.psp.TimeManager.models.User;
import com.psp.TimeManager.models.Workspace;
import com.psp.TimeManager.services.TaskService;
import com.psp.TimeManager.services.UserService;
import com.psp.TimeManager.services.WorkspaceService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final WorkspaceService workspaceService;
    private final WorkspaceMapper workspaceMapper;
    private final UserMapper userMapper;
    private final TaskService taskService;

    public UserController(UserService userService, WorkspaceService workspaceService, WorkspaceMapper workspaceMapper, UserMapper userMapper, TaskService taskService)
    {
        this.userService = userService;
        this.workspaceService = workspaceService;
        this.workspaceMapper = workspaceMapper;
        this.userMapper = userMapper;
        this.taskService = taskService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserPreviewDto>> getAllUsers()
    {
        List<UserPreviewDto> users = userMapper.mapToPreview(userService.findAllUsers());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/invite/{id}")
    public ResponseEntity<List<UserPreviewDto>> getUsersToInvite(@PathVariable int id)
    {
        Workspace workspace = workspaceService.findWorkspaceById(id);
        List<User> exceptUsers = workspace.getUsers();

        List<User> allUsers = userService.findAllUsers();
        List<User> result = new ArrayList<>(allUsers);
        result.removeIf(exceptUsers::contains);

        List<UserPreviewDto> users = userMapper.mapToPreview(result);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/toAppoint/{workspaceId}/{taskId}")
    public ResponseEntity<List<UserPreviewDto>> getUsersToAppoint(@PathVariable int workspaceId, @PathVariable int taskId)
    {
        Task task = taskService.findTaskById(taskId);
        List<User> exceptUsers = task.getAppointedUsers();

        Workspace workspace = workspaceService.findWorkspaceById(workspaceId);
        List<User> result = new ArrayList<>(workspace.getUsers());
        result.removeIf(exceptUsers::contains);

        List<UserPreviewDto> users = userMapper.mapToPreview(result);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    private boolean isDateOverlapping(LocalDateTime startDate1, LocalDateTime endDate1, LocalDateTime startDate2, LocalDateTime endDate2) {
        if (startDate1.isBefore(endDate2) && endDate1.isAfter(startDate2) ||
                startDate1.isBefore(endDate2) && endDate1.isEqual(endDate2) ||
                startDate1.isEqual(startDate2) && endDate1.isAfter(startDate2) ||
                startDate1.isEqual(startDate2) && endDate1.isEqual(endDate2)){
            return true;
        }else{
            return false;
        }
    }

    @GetMapping("/workspaces")
    public ResponseEntity<List<WorkspaceDto>> getUserWorkspaces(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth)
    {
        UserDto userDetails = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUserByEmail(userDetails.getEmail());
        List<WorkspaceDto> workspaces = workspaceMapper.toListWorkspaceDto(user.getWorkspaces());
        return new ResponseEntity<>(workspaces, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") int id)
    {
        User user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        userService.addUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/invite")
    public ResponseEntity<?> addUser(@RequestBody InviteDto inviteDto) {
        User user =  userService.findUserById(inviteDto.userId());
        Workspace workspace = workspaceService.findWorkspaceById(inviteDto.workspaceId());
        List<User> users = workspace.getUsers();
        users.add(user);
        workspace.setUsers(users);
        workspaceService.updateWorkspace(workspace);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User userForDB = userService.updateUser(user);
        return new ResponseEntity<>(userForDB, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
