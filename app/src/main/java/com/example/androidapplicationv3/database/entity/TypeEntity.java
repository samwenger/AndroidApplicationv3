package com.example.androidapplicationv3.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "types")
public class TypeEntity {

    @PrimaryKey(autoGenerate = true)
    private Long idType;

    private String type;

    public TypeEntity(String type) {
        this.type = type;
    }

    public Long getIdType() {
        return idType;
    }

    public String getType() {
        return type;
    }

    public void setIdType(Long idType) {
        this.idType = idType;
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
