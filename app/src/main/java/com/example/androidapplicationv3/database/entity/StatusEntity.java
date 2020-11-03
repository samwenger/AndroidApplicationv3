package com.example.androidapplicationv3.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "status")
public class StatusEntity {
    @PrimaryKey(autoGenerate = true)
    private Long idStatus;

    private String status;

    public StatusEntity() {
        this.status = status;
    }

    public Long getIdStatus() {
        return idStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setIdStatus(Long idStatus) {
        this.idStatus = idStatus;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatusEntity that = (StatusEntity) o;
        return Objects.equals(idStatus, that.idStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idStatus);
    }

}
