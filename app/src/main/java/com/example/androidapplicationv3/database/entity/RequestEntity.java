package com.example.androidapplicationv3.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "request",
        foreignKeys = {
                @ForeignKey(
                        entity = UserEntity.class,
                        parentColumns = "idUser",
                        childColumns = "idUser"
                ),
                @ForeignKey(
                        entity = StatusEntity.class,
                        parentColumns = "idStatus",
                        childColumns = "idStatus"
                ),
                @ForeignKey(
                        entity = TypeEntity.class,
                        parentColumns = "idType",
                        childColumns = "idType"
                )}
)
public class RequestEntity {

    @PrimaryKey(autoGenerate = true)
    private Long idRequest;

    private Long idUser;

    private Long dateDebut;

    private Long dateFin;

    private Long idStatus;

    private Long idType;

    public RequestEntity(Long idUser, Long dateDebut, Long dateFin, Long idStatus, Long idType) {
        this.idUser = idUser;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.idStatus = idStatus;
        this.idType = idType;
    }

    public Long getIdUser() {
        return idUser;
    }

    public Long getIdType() {
        return idType;
    }

    public Long getDateDebut() {
        return dateDebut;
    }

    public Long getDateFin() {
        return dateFin;
    }

    public Long getIdRequest() {
        return idRequest;
    }

    public Long getIdStatus() {
        return idStatus;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public void setIdType(Long idType) {
        this.idType = idType;
    }

    public void setDateDebut(Long dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(Long dateFin) {
        this.dateFin = dateFin;
    }

    public void setIdRequest(Long idRequest) {
        this.idRequest = idRequest;
    }

    public void setIdStatus(Long idStatus) {
        this.idStatus = idStatus;
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
}
