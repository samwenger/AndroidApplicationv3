package com.example.androidapplicationv3.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.Objects;

@Entity(tableName = "types", primaryKeys = "idType")
public class TypeEntity {

    @NonNull
    private Long idType;

    @NonNull
    private String type;

    public TypeEntity(Long idType, String type) {
        this.idType=idType;
        this.type = type;
    }

    public Long getIdType() {
        return idType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeEntity that = (TypeEntity) o;
        return Objects.equals(idType, that.idType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idType);
    }
}
