package com.psp.TimeManager.services;

import com.psp.TimeManager.dtos.UserDto;
import com.psp.TimeManager.dtos.WorkspaceDto;
import com.psp.TimeManager.exceptions.UserNotFoundException;
import com.psp.TimeManager.mappers.WorkspaceMapper;
import com.psp.TimeManager.models.User;
import com.psp.TimeManager.models.Workspace;
import com.psp.TimeManager.repositories.UserRepository;
import com.psp.TimeManager.repositories.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final WorkspaceMapper workspaceMapper;

    @Autowired
    public WorkspaceService(WorkspaceRepository workspaceRepository, UserRepository userRepository, WorkspaceMapper workspaceMapper)
    {
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
        this.workspaceMapper = workspaceMapper;
    }

    public Workspace addWorkspace(WorkspaceDto workspace)
    {
        Workspace savedWorkspace = workspaceMapper.toWorkspace(workspace);
        UserDto userDetails = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findByEmail(userDetails.getEmail());
        user.ifPresent(u -> savedWorkspace.setAuthor(u));
        List<User> users = savedWorkspace.getUsers();
        user.ifPresent(u -> users.add(u));
        savedWorkspace.setUsers(users);
        return workspaceRepository.save(savedWorkspace);
    }

    public List<Workspace> findUserWorkspaces(){
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
