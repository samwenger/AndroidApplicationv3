package com.example.androidapplicationv3.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.sql.Date;
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
                        parentColumns = "idRole",
                        childColumns = "idRole"
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

    private Date dateDebut;

    private Date dateFin;

    private Long idStatus;

    private Long idType;

    public RequestEntity(Long idUser, Date dateDebut, Date dateFin, Long idStatus, Long idType) {
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

    public Date getDateDebut() {
        return dateDebut;
    }

    public Date getDateFin() {
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

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(Date dateFin) {
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
