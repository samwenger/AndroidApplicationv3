package com.example.androidapplicationv3.database.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TypeEntity {

    private String idType;
    private String type;

    public TypeEntity() {

    }

    public TypeEntity(String idType, String type) {
        this.idType=idType;
        this.type = type;
    }

    @Exclude
    public String getIdType() {
        return idType;
    }

    public String getType() {
        return type;
    }

    public void setIdType(String idType) {
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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", type);
        return result;
    }
}
