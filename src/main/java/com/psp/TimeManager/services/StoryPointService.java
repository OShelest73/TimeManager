package com.psp.TimeManager.services;

import com.psp.TimeManager.models.StoryPoint;
import com.psp.TimeManager.repositories.StoryPointRepository;
import org.springframework.stereotype.Service;

@Service
public class StoryPointService {
    private final StoryPointRepository storyPointRepository;

    public StoryPointService(StoryPointRepository storyPointRepository) {
        this.storyPointRepository = storyPointRepository;
    }

    public StoryPoint findStoryPointById(int id)
    {
        return storyPointRepository.findStoryPointById(id);
    }
}
