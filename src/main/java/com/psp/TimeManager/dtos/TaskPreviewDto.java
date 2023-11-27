package com.psp.TimeManager.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record TaskPreviewDto(int id, String taskName, @JsonFormat(pattern="MM-dd-yyyy HH:mm")LocalDateTime startDate,
                             @JsonFormat(pattern="MM-dd-yyyy HH:mm")LocalDateTime finishDate, String status) {
}
