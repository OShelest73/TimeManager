package com.psp.TimeManager.dtos;

import java.time.LocalDateTime;

public record TaskPreviewDto(int id, String taskName, LocalDateTime finishDate, String status, int storyPoint) {
}
