package com.psp.TimeManager.dtos;

import com.psp.TimeManager.models.User;

import java.time.LocalDateTime;
import java.util.List;

public record CreateTaskDto(String taskName, String description, String notes, LocalDateTime startDate,
                            LocalDateTime finishDate, List<User> appointedUsers, int workspaceId, Integer storyPoint) {
}
