package com.psp.TimeManager.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record TaskDetailsDto(int id, String taskName, String description, String notes, @JsonFormat(pattern="dd-MM-yyyy HH:mm") LocalDateTime startDate,
                             @JsonFormat(pattern="dd-MM-yyyy HH:mm")LocalDateTime finishDate, String status, int storyPoint) {
}
