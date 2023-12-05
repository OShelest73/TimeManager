package com.psp.TimeManager.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private int id;
    @Column(nullable = false)
    private String taskName;
    @Column(nullable = false)
    private String description;
    @Column
    private String notes;
    @Column
    private Boolean isAccepted = null;
    @Column
    private boolean isExpired = false;
    @Column
    private boolean isToCheck = false;

    @Column(nullable = false)
    private LocalDateTime startDate;
    @Column(nullable = false)
    private LocalDateTime finishDate;

    private String status;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToMany
    @JoinTable(
        name = "task_user",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    List<User> appointedUsers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @ManyToOne
    @JoinColumn(name = "story_point")
    private StoryPoint storyPoint;

    @PostLoad
    public void initializeStatus(){
        //По сути фильтр, порядок важен. Сначала есть ли вообще выполняющие, потом отрезаем по времени (просрочена->выполняется->предстоит)
        if (isAccepted != null)
        {
            if (isAccepted == false)
            {
                status = "Отклонено";
                return;
            }
            if (isAccepted == true)
            {
                status = "Завершено";
                return;
            }
        }
        if (appointedUsers == null || appointedUsers.size() == 0)
        {
            status = "Без исполнителя";
            return;
        }
        LocalDateTime currentDate = LocalDateTime.now();
        if (currentDate.compareTo(finishDate) > 0)
        {
            status = "Просрочено";
            isExpired = true;
            return;
        }
        if (currentDate.compareTo(startDate) >= 0)
        {
            status = "Выполняется";
            return;
        }
        if (currentDate.compareTo(startDate) < 0)
        {
            status = "Предстоит";
            return;
        }
    }
}
