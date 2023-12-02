package com.psp.TimeManager.mappers;

import com.psp.TimeManager.dtos.TaskDetailsDto;
import com.psp.TimeManager.dtos.TaskPreviewDto;
import com.psp.TimeManager.dtos.WorkspaceDto;
import com.psp.TimeManager.models.*;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueMapMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface TaskMapper {
    TaskPreviewDto toTaskPreviewDto(Task task);
    List<TaskPreviewDto> toListTaskPreviewDto(List<Task> tasks);
    TaskDetailsDto toTaskDetails(Task task);

    default int mapStoryPoint(StoryPoint storyPoint){
        int storyPointDto = 0;
        if (storyPoint != null)
        {
            storyPointDto = storyPoint.getId();
        }
        return storyPointDto;
    }
}
