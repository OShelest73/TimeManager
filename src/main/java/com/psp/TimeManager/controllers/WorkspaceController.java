package com.psp.TimeManager.controllers;

import com.psp.TimeManager.dtos.TaskPreviewDto;
import com.psp.TimeManager.dtos.WorkspaceDto;
import com.psp.TimeManager.mappers.TaskMapper;
import com.psp.TimeManager.models.Workspace;
import com.psp.TimeManager.services.WorkspaceService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workspace")
public class WorkspaceController {
    private final WorkspaceService workspaceService;
    private final TaskMapper taskMapper;

    public WorkspaceController(WorkspaceService workspaceService, TaskMapper taskMapper)
    {
        this.workspaceService = workspaceService;
        this.taskMapper = taskMapper;
    }

    @GetMapping("/all") //TODO в теории можно сносить
    public ResponseEntity<List<Workspace>> getUserWorkspaces()
    {
        List<Workspace> workspaces = workspaceService.findUserWorkspaces();
        return new ResponseEntity<>(workspaces, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Workspace> getWorkspace(@PathVariable("id") int id)
    {
        Workspace workspace = workspaceService.findWorkspaceById(id);
        return new ResponseEntity<>(workspace, HttpStatus.OK);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<List<TaskPreviewDto>> getWorkspaceTasks(@PathVariable("id") int workspaceId)
    {
        Workspace workspace = workspaceService.findWorkspaceById(workspaceId);
        List<TaskPreviewDto> tasks = taskMapper.toListTaskPreviewDto(workspace.getWorkspaceTasks());
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<WorkspaceDto> addWorkspace(@RequestBody WorkspaceDto workspaceDto) {
        Workspace workspaceForDB = workspaceService.addWorkspace(workspaceDto);
        return new ResponseEntity<>(workspaceDto, HttpStatus.CREATED);
    }

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
