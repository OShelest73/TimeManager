package com.psp.TimeManager.mappers;

import com.psp.TimeManager.dto.UserDto;
import com.psp.TimeManager.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);

}
