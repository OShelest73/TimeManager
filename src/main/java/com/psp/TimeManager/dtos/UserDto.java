package com.psp.TimeManager.dtos;

import com.psp.TimeManager.models.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private int id;
    private String fullName;
    private String email;
    private String token;
    private String jobTitle;
    private List<String> permissions = new ArrayList<>();
}
