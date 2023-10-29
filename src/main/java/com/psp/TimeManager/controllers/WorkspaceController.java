package com.psp.TimeManager.controllers;

import com.psp.TimeManager.models.Workspace;
import com.psp.TimeManager.services.WorkspaceService;
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
    public ResponseEntity<List<Workspace>> getAllWorkspaces()
    {
        List<Workspace> workspaces = workspaceService.findAllUsers();
        return new ResponseEntity<>(workspaces, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Workspace> getWorkspace(@PathVariable("id") int id)
    {
        Workspace workspace = workspaceService.findWorkspaceById(id);
        return new ResponseEntity<>(workspace, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Workspace> addWorkspace(@RequestBody Workspace workspace) {
        Workspace workspaceForDB = workspaceService.addWorkspace(workspace);
        return new ResponseEntity<>(workspaceForDB, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Workspace> updateWorkspace(@RequestBody Workspace workspace) {
        Workspace workspaceForDB = workspaceService.updateWorkspace(workspace);
        return new ResponseEntity<>(workspaceForDB, HttpStatus.OK);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> deleteWorkspace(@PathVariable("id") int id) {
        workspaceService.deleteWorkspace(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}