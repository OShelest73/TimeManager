package com.psp.TimeManager.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
public class StoryPoint {
    @Id
    @Column(nullable = false, updatable = false)
    private int id;
}
