package com.psp.TimeManager.mappers;

import com.psp.TimeManager.dto.SignUpDto;
import com.psp.TimeManager.dto.UserDto;
import com.psp.TimeManager.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);
}
