package com.psp.TimeManager.repositories;

import com.psp.TimeManager.models.User;
import com.psp.TimeManager.models.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkspaceRepository extends JpaRepository<Workspace, Integer> {
    void deleteWorkspaceById(int id);

    Optional<Workspace> findWorkspaceById(int id);
}
