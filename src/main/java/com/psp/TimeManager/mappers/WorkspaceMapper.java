package com.psp.TimeManager.mappers;

import com.psp.TimeManager.dtos.WorkspaceDto;
import com.psp.TimeManager.models.Workspace;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkspaceMapper {
    WorkspaceDto toWorkspaceDto(Workspace workspace);
    Workspace toWorkspace(WorkspaceDto workspaceDto);
    List<WorkspaceDto> toListWorkspaceDto(List<Workspace> workspaces);
}
