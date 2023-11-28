package com.psp.TimeManager.services;

import com.psp.TimeManager.models.JobTitle;
import com.psp.TimeManager.models.User;
import com.psp.TimeManager.repositories.JobTitleRepository;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;

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
}
