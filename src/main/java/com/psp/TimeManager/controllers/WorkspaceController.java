package com.psp.TimeManager.controllers;

import com.psp.TimeManager.dtos.WorkspaceDto;
import com.psp.TimeManager.models.User;
import com.psp.TimeManager.models.Workspace;
import com.psp.TimeManager.services.UserService;
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

    public WorkspaceController(WorkspaceService workspaceService)
    {
        this.workspaceService = workspaceService;
    }

    @GetMapping("/all")
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
