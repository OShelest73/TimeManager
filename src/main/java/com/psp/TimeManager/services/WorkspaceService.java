package com.psp.TimeManager.services;

import com.psp.TimeManager.exceptions.UserNotFoundException;
import com.psp.TimeManager.models.User;
import com.psp.TimeManager.models.Workspace;
import com.psp.TimeManager.repositories.UserRepository;
import com.psp.TimeManager.repositories.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;

    @Autowired
    public WorkspaceService(WorkspaceRepository workspaceRepository)
    {
        this.workspaceRepository = workspaceRepository;
    }

    public Workspace addWorkspace(Workspace workspace)
    {
        return workspaceRepository.save(workspace);
    }

    public List<Workspace> findAllUsers(){
        return workspaceRepository.findAll();
    }

    public Workspace updateWorkspace(Workspace user)
    {
        return workspaceRepository.save(user);
    }

    public Workspace findWorkspaceById(int id)
    {
        return workspaceRepository.findWorkspaceById(id).
                orElseThrow(() -> new UserNotFoundException("Workspace was not found")); //TODO change exception (name or create another)
    }

    public void deleteWorkspace(int id){
        workspaceRepository.deleteWorkspaceById(id);
    }
}
