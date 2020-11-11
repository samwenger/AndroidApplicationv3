package com.example.androidapplicationv3.database.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.androidapplicationv3.database.entity.RequestEntity;
import com.example.androidapplicationv3.database.entity.StatusEntity;
import com.example.androidapplicationv3.database.entity.TypeEntity;
import com.example.androidapplicationv3.database.entity.UserEntity;

import java.text.SimpleDateFormat;

public class RequestWithUser {

    @Embedded
    public RequestEntity request;

    @Relation(parentColumn = "idType", entityColumn = "idType", entity = TypeEntity.class)
    public TypeEntity type;

    @Relation(parentColumn = "idStatus", entityColumn = "idStatus", entity = StatusEntity.class)
    public StatusEntity status;

    @Relation(parentColumn = "idUser", entityColumn = "idUser", entity = UserEntity.class)
    public UserEntity user;


    @Override
    public String toString() {

        String dateDebut = new SimpleDateFormat("dd/MM/yyyy").format(request.getDateDebut());
        String dateFin = new SimpleDateFormat("dd/MM/yyyy").format(request.getDateFin());

        return user.getLastname() + " " + user.getFirstname() + " ("
                + dateDebut + " - " + dateFin + ") (" +
                type.getType() + ")";
    }

}
