package com.psp.TimeManager.dtos;

import com.psp.TimeManager.models.JobTitle;

public record UserPreviewDto(int id, String fullName, String email, String jobTitle) {

}
