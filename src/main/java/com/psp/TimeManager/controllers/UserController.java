package com.psp.TimeManager.controllers;

import com.psp.TimeManager.dtos.UserDto;
import com.psp.TimeManager.dtos.UserPreviewDto;
import com.psp.TimeManager.dtos.WorkspaceDto;
import com.psp.TimeManager.mappers.UserMapper;
import com.psp.TimeManager.mappers.WorkspaceMapper;
import com.psp.TimeManager.models.User;
import com.psp.TimeManager.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final WorkspaceMapper workspaceMapper;
    private final UserMapper userMapper;

    public UserController(UserService userService, WorkspaceMapper workspaceMapper, UserMapper userMapper)
    {
        this.userService = userService;
        this.workspaceMapper = workspaceMapper;
        this.userMapper = userMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserPreviewDto>> getAllUsers()
    {
        List<UserPreviewDto> users = userMapper.mapToPreview(userService.findAllUsers());
        return new ResponseEntity<>(users, HttpStatus.OK);
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
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User userForDB = userService.addUser(user);
        return new ResponseEntity<>(userForDB, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User userForDB = userService.updateUser(user);
        return new ResponseEntity<>(userForDB, HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
