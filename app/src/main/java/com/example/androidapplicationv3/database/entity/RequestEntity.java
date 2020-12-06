package com.example.androidapplicationv3.database.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RequestEntity {

    private String idRequest;

    private Long dateDebut;

    private Long dateFin;

    private String idStatus;

    private String idType;

    private String remark;

    private String idUser;

    public RequestEntity(){
    }

    public RequestEntity(String idUser, Long dateDebut, Long dateFin, String idStatus, String idType, String remark) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.idStatus = idStatus;
        this.idType = idType;
        this.remark=remark;
        this.idUser = idUser;
    }

    public String getIdType() {
        return idType;
    }

    public Long getDateDebut() {
        return dateDebut;
    }

    public Long getDateFin() {
        return dateFin;
    }

    @Exclude
    public String getIdRequest() {
        return idRequest;
    }

    public String getIdStatus() {
        return idStatus;
    }

    public String getRemark() {
        return remark;
    }

    @Exclude
    public String getIdUser() {
        return idUser;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public void setDateDebut(Long dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(Long dateFin) {
        this.dateFin = dateFin;
    }

    public void setIdRequest(String idRequest) {
        this.idRequest = idRequest;
    }

    public void setIdStatus(String idStatus) {
        this.idStatus = idStatus;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestEntity that = (RequestEntity) o;
        return Objects.equals(idRequest, that.idRequest);
    }


    @Override
    public int hashCode() {
        return Objects.hash(idRequest);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("dateDebut", dateDebut);
        result.put("dateFin", dateFin);
        result.put("status", idStatus);
        result.put("type", idType);
        result.put("remark", remark);
        return result;
    }

}
