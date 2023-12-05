package com.psp.TimeManager.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record TaskReportDto(int id, @JsonFormat(pattern="dd-MM-yyyy HH:mm")LocalDateTime startDate, @JsonFormat(pattern="dd-MM-yyyy HH:mm")LocalDateTime finishDate,
                            int teamMembersAmount, Boolean isAccepted, boolean isExpired, int storyPoint) {
}
