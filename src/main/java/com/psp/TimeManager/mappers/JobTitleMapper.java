package com.psp.TimeManager.mappers;

import com.psp.TimeManager.dtos.JobTitleDto;
import com.psp.TimeManager.models.JobTitle;
import com.psp.TimeManager.models.Permission;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface JobTitleMapper {
    default List<String> jobTitleToStringList(List<JobTitle> jobTitles){
        List<String> mappedTitles = new ArrayList<>();
        for(JobTitle title: jobTitles){
            mappedTitles.add(title.getTitle());
        }
        return mappedTitles;
    }
    List<JobTitleDto> jobTitleToSend(List<JobTitle> titles);

    default List<String> mapPermissions(List<Permission> permissions){
        List<String> mappedPermissions = new ArrayList<>();
        for(Permission permission: permissions)
        {
            mappedPermissions.add(permission.getPermissionName());
        }
        return mappedPermissions;
    }
}
