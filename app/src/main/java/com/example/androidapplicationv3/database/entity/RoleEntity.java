package com.example.androidapplicationv3.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "roles")
public class RoleEntity {
    @PrimaryKey(autoGenerate = true)
    private Long idRole;

    private String role;

    public RoleEntity (String role) {
        this.role = role;
    }

    public Long getIdRole() {
        return idRole;
    }

    public void setIdRole(Long idRole) {
        this.idRole = idRole;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleEntity that = (RoleEntity) o;
        return Objects.equals(idRole, that.idRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRole);
    }
}
