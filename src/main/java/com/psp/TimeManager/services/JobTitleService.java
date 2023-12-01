package com.psp.TimeManager.services;

import com.psp.TimeManager.exceptions.UserNotFoundException;
import com.psp.TimeManager.models.JobTitle;
import com.psp.TimeManager.repositories.JobTitleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobTitleService {
    private final JobTitleRepository jobTitleRepository;

    public JobTitleService(JobTitleRepository jobTitleRepository) {
        this.jobTitleRepository = jobTitleRepository;
    }

    public JobTitle addJobTitle(JobTitle jobTitle)
    {
        return jobTitleRepository.save(jobTitle);
    }

    public JobTitle updateJobTitle(JobTitle jobTitle)
    {
        return jobTitleRepository.save(jobTitle);
    }

    public JobTitle findTitleById(String id)
    {
        return jobTitleRepository.findByTitle(id)
                .orElseThrow(() -> new UserNotFoundException("Job title was not found"));
    }

    public List<JobTitle> findAllJobTitles(){
        return jobTitleRepository.findAll();
    }

    public void deleteJobTitle(String id){
        jobTitleRepository.deleteTitleByTitle(id);
    }
}
