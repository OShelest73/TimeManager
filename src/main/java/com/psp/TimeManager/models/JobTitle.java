package com.psp.TimeManager.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class JobTitle implements Serializable {

    public JobTitle(String title)
    {
        this.title = title;
    }

    @Id
    private String title;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "jobTitle_permission",
            joinColumns = @JoinColumn(name = "jobTitle"),
            inverseJoinColumns = @JoinColumn(name = "permission"))
    private List<Permission> permissions = new ArrayList<>();
}
