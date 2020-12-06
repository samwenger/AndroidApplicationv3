package com.example.androidapplicationv3.database.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StatusEntity {

    private String idStatus;
    private String status;

    public StatusEntity(String idStatus, String status) {
        this.idStatus=idStatus;
        this.status = status;
    }

    @Exclude
    public String getIdStatus() {
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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", status);
        return result;
    }

}
