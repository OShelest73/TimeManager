package com.psp.TimeManager.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
public class Permission implements Serializable {
    @Id
    private String permissionName;

    public Permission(String permissionName)
    {
        this.permissionName = permissionName;
    }
}
