package com.psp.TimeManager.mappers;

import com.psp.TimeManager.dtos.TaskPreviewDto;
import com.psp.TimeManager.dtos.TaskReportDto;
import com.psp.TimeManager.models.Task;
import com.psp.TimeManager.models.TaskReport;
import com.psp.TimeManager.repositories.TaskReportRepository;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskReportMapper {
    List<TaskReportDto> toListTaskReportDto(List<TaskReport> reports);

    default TaskReportDto toTaskReportDto(TaskReport taskReport){
        TaskReportDto taskReportDto = new TaskReportDto(
                taskReport.getId(),
                taskReport.getTask().getStartDate(),
                taskReport.getTask().getFinishDate(),
                taskReport.getTeamMembersAmount(),
                taskReport.getTask().getIsAccepted(),
                taskReport.getTask().isExpired(),
                taskReport.getTask().getStoryPoint().getId()
        );
        return taskReportDto;
    }
}
