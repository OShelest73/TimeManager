package com.psp.TimeManager.controllers;

import com.psp.TimeManager.dtos.JobTitleDto;
import com.psp.TimeManager.mappers.JobTitleMapper;
import com.psp.TimeManager.mappers.UserMapper;
import com.psp.TimeManager.models.JobTitle;
import com.psp.TimeManager.models.User;
import com.psp.TimeManager.services.JobTitleService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobTitle")
public class JobTitleController {
    private final JobTitleService jobTitleService;
    private final JobTitleMapper jobTitleMapper;

    public JobTitleController(JobTitleService jobTitleService, JobTitleMapper jobTitleMapper) {
        this.jobTitleService = jobTitleService;
        this.jobTitleMapper = jobTitleMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<List<String>> getAllJobTitles() {
        List<JobTitle> jobTitles = jobTitleService.findAllJobTitles();
        List<String> mappedJobTitles = jobTitleMapper.jobTitleToStringList(jobTitles);
        return new ResponseEntity<>(mappedJobTitles, HttpStatus.OK);
    }

    @GetMapping("/allAsObj")
    public ResponseEntity<List<JobTitleDto>> getJobTitlesAsObj() {
        List<JobTitleDto> jobTitles = jobTitleMapper.jobTitleToSend(jobTitleService.findAllJobTitles());
        return new ResponseEntity<>(jobTitles, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addJobTitle(@RequestBody JobTitle jobTitle) {
        JobTitle titleForDB = jobTitleService.addJobTitle(jobTitle);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<JobTitle> updateJobTitle(@RequestBody JobTitle jobTitle) {
        JobTitle titleForDb = jobTitleService.updateJobTitle(jobTitle);
        return new ResponseEntity<>(titleForDb, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteJobTitle(@PathVariable String id) {
        JobTitle titleToDelete = jobTitleService.findTitleById(id);
        titleToDelete.setPermissions(null);
        jobTitleService.updateJobTitle(titleToDelete);
        jobTitleService.deleteJobTitle(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
