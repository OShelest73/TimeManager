package com.psp.TimeManager.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

@Data
@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private int id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "jobTitle_id")
    private JobTitle jobTitle;

    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private List<Workspace> workspaces = new ArrayList<>();

    @PreRemove
    private void removeWorkspaceAssociations() {
        for (Workspace workspace: this.workspaces) {
            workspace.getUsers().remove(this);
        }
    }
}
