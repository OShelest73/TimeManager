package com.psp.TimeManager.repositories;

import com.psp.TimeManager.models.JobTitle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobTitleRepository extends JpaRepository<JobTitle, String> {
}
