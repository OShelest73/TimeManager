package com.psp.TimeManager.mappers;

import com.psp.TimeManager.dtos.SignUpDto;
import com.psp.TimeManager.dtos.UserDto;
import com.psp.TimeManager.dtos.UserPreviewDto;
import com.psp.TimeManager.models.JobTitle;
import com.psp.TimeManager.models.Permission;
import com.psp.TimeManager.models.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    default UserDto toUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setFullName(user.getFullName());
        userDto.setPermissions(this.mapPermissions(user.getJobTitle().getPermissions()));
        return userDto;
    };

    List<String> mapPermissions(List<Permission> permissions);

    List<UserPreviewDto> mapToPreview(List<User> users);
    List<User> mapFromPreview(List<UserPreviewDto> users);


    default List<Permission> jobTitleToPermissions(JobTitle jobTitle)
    {
        List<Permission> permissions = jobTitle.getPermissions();
        return permissions;
    }

    default String mapPermission(Permission permission){
        String permissionDto = new String(permission.getPermissionName());
        return permissionDto;
    }

    default String mapJobTitle(JobTitle jobTitle){
        String jobTitleDto = new String(jobTitle.getTitle());
        return jobTitleDto;
    }

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);
}
