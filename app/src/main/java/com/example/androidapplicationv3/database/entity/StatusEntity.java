package com.example.androidapplicationv3.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.Objects;

@Entity(tableName = "status",
        primaryKeys ="idStatus")

public class StatusEntity {
    @NonNull
    private Long idStatus;

    @NonNull
    private String status;

    public StatusEntity(Long idStatus, String status) {
        this.idStatus=idStatus;
        this.status = status;
    }

    public Long getIdStatus() {
        return idStatus;
    }

    public String getStatus() {
        return status;
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
