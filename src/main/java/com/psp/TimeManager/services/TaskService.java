package com.psp.TimeManager.services;

import com.psp.TimeManager.dtos.CreateTaskDto;
import com.psp.TimeManager.dtos.UserDto;
import com.psp.TimeManager.exceptions.UserNotFoundException;
import com.psp.TimeManager.models.Task;
import com.psp.TimeManager.models.User;
import com.psp.TimeManager.models.Workspace;
import com.psp.TimeManager.repositories.TaskRepository;
import com.psp.TimeManager.repositories.UserRepository;
import com.psp.TimeManager.repositories.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final StoryPointService storyPointService;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository, WorkspaceRepository workspaceRepository, StoryPointService storyPointService)
    {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.workspaceRepository = workspaceRepository;
        this.storyPointService = storyPointService;
    }

    public Task addTask(CreateTaskDto task)
    {
        Task taskToDb = new Task();
        taskToDb.setTaskName(task.taskName());
        taskToDb.setDescription(task.description());
        taskToDb.setNotes(task.notes());
        taskToDb.setStartDate(task.startDate());
        taskToDb.setFinishDate(task.finishDate());
        Optional<Workspace> workspace = workspaceRepository.findWorkspaceById(task.workspaceId());
        workspace.ifPresent(w -> taskToDb.setWorkspace(w));
        if (task.storyPoint() != null)
        {
           taskToDb.setStoryPoint(storyPointService.findStoryPointById(task.storyPoint().intValue()));
        }
        UserDto userDetails = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findByEmail(userDetails.getEmail());
        user.ifPresent(u -> taskToDb.setAuthor(u));
        //TODO чтобы ещё список юзеров можно было запихивать (ну чтобы можно было назначать людей)

        return taskRepository.save(taskToDb);
    }

    public List<Task> findAllTasks(){
        return taskRepository.findAll();
    }

    public Task updateTask(Task task)
    {
        UserDto userDetails = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findByEmail(userDetails.getEmail());
        user.ifPresent(u -> task.setAuthor(u));

        return taskRepository.save(task);
    }

    public Task findTaskById(int id)
    {
        return taskRepository.findTaskById(id).
                orElseThrow(() -> new UserNotFoundException("Task was not found"));
    }

    public void deleteTask(int id){
        taskRepository.deleteTaskById(id);
    }
}
