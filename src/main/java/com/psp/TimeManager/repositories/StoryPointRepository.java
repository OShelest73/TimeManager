package com.psp.TimeManager.repositories;

import com.psp.TimeManager.models.StoryPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryPointRepository extends JpaRepository<StoryPoint, Integer> {
    StoryPoint findStoryPointById(int id);
}
