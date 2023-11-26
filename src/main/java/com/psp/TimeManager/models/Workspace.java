package com.psp.TimeManager.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Workspace implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String evaluationMethod;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    @OneToMany
    @JoinColumn(name = "workspace_id")
    List<Task> workspaceTasks = new ArrayList<>();
    @ManyToMany
    @JoinTable(name = "workspace_user",
            joinColumns = @JoinColumn(name = "workspace_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users = new ArrayList<>();

    public void removeUser(User user){
        this.users.remove(user);
        user.getWorkspaces().remove(this);
    }
}
