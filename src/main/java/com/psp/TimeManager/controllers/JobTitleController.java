package com.psp.TimeManager.controllers;

import com.psp.TimeManager.models.JobTitle;
import com.psp.TimeManager.models.User;
import com.psp.TimeManager.services.JobTitleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobTitle")
public class JobTitleController {
    private final JobTitleService jobTitleService;

    public JobTitleController(JobTitleService jobTitleService) {
        this.jobTitleService = jobTitleService;
    }

    @PostMapping("/add")
    public ResponseEntity<JobTitle> addJobTitle(@RequestBody JobTitle jobTitle) {
        JobTitle titleForDb = jobTitleService.addJobTitle(jobTitle);
        return new ResponseEntity<>(titleForDb, HttpStatus.CREATED);
    }
}
