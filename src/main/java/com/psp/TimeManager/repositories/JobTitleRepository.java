package com.psp.TimeManager.repositories;

import com.psp.TimeManager.models.JobTitle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobTitleRepository extends JpaRepository<JobTitle, String> {
    Optional<JobTitle> findByTitle(String title);

    void deleteTitleByTitle(String title);
}
