package com.psp.TimeManager.mappers;

import com.psp.TimeManager.dtos.SignUpDto;
import com.psp.TimeManager.dtos.UserDto;
import com.psp.TimeManager.models.Permission;
import com.psp.TimeManager.models.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);

    List<String> map(List<Permission> permissions);

    default String map(Permission permission){
        String permissionDto = new String(permission.getPermissionName());
        return permissionDto;
    }

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);
}
