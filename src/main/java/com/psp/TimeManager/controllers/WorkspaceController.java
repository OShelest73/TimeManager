package com.psp.TimeManager.controllers;

import com.psp.TimeManager.dtos.TaskPreviewDto;
import com.psp.TimeManager.dtos.UserPreviewDto;
import com.psp.TimeManager.dtos.WorkspaceDto;
import com.psp.TimeManager.mappers.TaskMapper;
import com.psp.TimeManager.mappers.UserMapper;
import com.psp.TimeManager.mappers.WorkspaceMapper;
import com.psp.TimeManager.models.Task;
import com.psp.TimeManager.models.User;
import com.psp.TimeManager.models.Workspace;
import com.psp.TimeManager.services.WorkspaceService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/workspace")
public class WorkspaceController {
    private final WorkspaceService workspaceService;
    private final TaskMapper taskMapper;
    private final UserMapper userMapper;
    private final WorkspaceMapper workspaceMapper;

    public WorkspaceController(WorkspaceService workspaceService, TaskMapper taskMapper, UserMapper userMapper, WorkspaceMapper workspaceMapper)
    {
        this.workspaceService = workspaceService;
        this.taskMapper = taskMapper;
        this.userMapper = userMapper;
        this.workspaceMapper = workspaceMapper;
    }

    @GetMapping("/all") //TODO в теории можно сносить
    public ResponseEntity<List<Workspace>> getUserWorkspaces()
    {
        List<Workspace> workspaces = workspaceService.findUserWorkspaces();
        return new ResponseEntity<>(workspaces, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<WorkspaceDto> getWorkspace(@PathVariable("id") int id)
    {
        WorkspaceDto workspace = workspaceMapper.toWorkspaceDto(workspaceService.findWorkspaceById(id));
        return new ResponseEntity<>(workspace, HttpStatus.OK);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<List<TaskPreviewDto>> getWorkspaceTasks(@PathVariable("id") int workspaceId)
    {
        Workspace workspace = workspaceService.findWorkspaceById(workspaceId);
        List<Task> tasks = workspace.getWorkspaceTasks();
        List<TaskPreviewDto> previewTasks = new ArrayList<>();

        for (Task task: tasks) {
            TaskPreviewDto previewTask = new TaskPreviewDto(
                    task.getId(),
                    task.getTaskName(),
                    task.getStartDate(),
                    task.getFinishDate(),
                    task.getStatus()
            );
            previewTasks.add(previewTask);
        }

        return new ResponseEntity<>(previewTasks, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<WorkspaceDto> addWorkspace(@RequestBody WorkspaceDto workspaceDto) {
        Workspace workspaceForDB = workspaceService.addWorkspace(workspaceDto);
        return new ResponseEntity<>(workspaceDto, HttpStatus.CREATED);
    }
/*-------------------------------------------------------------------------------------------------------------------------------*/
    @GetMapping("/users/{id}")
    public ResponseEntity<List<UserPreviewDto>> getWorkspaceUsers(@PathVariable("id") int workspaceId) {
        Workspace workspace = workspaceService.findWorkspaceById(workspaceId);
        List<UserPreviewDto> users = userMapper.mapToPreview(workspace.getUsers());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/addUsers/{id}")
    public ResponseEntity<?> addUsersToWorkspace(@PathVariable("id") int workspaceId, @RequestBody List<UserPreviewDto> users) {
        Workspace workspace = workspaceService.findWorkspaceById(workspaceId);
        List<User> usersToDb = workspace.getUsers();
        List<User> usersToAdd = userMapper.mapFromPreview(users);
        usersToDb.addAll(usersToAdd);
        workspace.setUsers(usersToDb);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    /*-------------------------------------------------------------------------------------------------------------------------------*/

    @PutMapping("/update")
    public ResponseEntity<Workspace> updateWorkspace(@RequestBody Workspace workspace) {
        Workspace workspaceForDB = workspaceService.updateWorkspace(workspace);
        return new ResponseEntity<>(workspaceForDB, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteWorkspace(@PathVariable("id") int id) {
        Workspace workspace = workspaceService.findWorkspaceById(id);
        workspace.getUsers().clear();

        workspaceService.deleteWorkspace(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
