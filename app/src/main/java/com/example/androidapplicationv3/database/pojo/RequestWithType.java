package com.example.androidapplicationv3.database.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.androidapplicationv3.database.entity.RequestEntity;
import com.example.androidapplicationv3.database.entity.StatusEntity;
import com.example.androidapplicationv3.database.entity.TypeEntity;

public class RequestWithType {
    @Embedded
    public RequestEntity request;

    @Relation(parentColumn = "idType", entityColumn = "idType", entity = TypeEntity.class)
    public TypeEntity type;

    @Relation(parentColumn = "idStatus", entityColumn = "idStatus", entity = StatusEntity.class)
    public StatusEntity status;

}
