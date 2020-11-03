package com.example.androidapplicationv3.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "user-role",
        primaryKeys = {
        "idUser","idRole"
        },
        foreignKeys = {
        @ForeignKey(
                entity = UserEntity.class,
                parentColumns = "idUser",
                childColumns = "idUser"
        ),
        @ForeignKey(
                entity = RoleEntity.class,
                parentColumns = "idRole",
                childColumns = "idRole"
        )}
)
public class UserRoleEntity {
    @NonNull
    private Long idUser;
    @NonNull
    private Long idRole;

    public UserRoleEntity(Long idUser, Long idRole) {
        this.idUser = idUser;
        this.idRole = idRole;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdRole() {
        return idRole;
    }

    public void setIdRole(Long idRole) {
        this.idRole = idRole;
    }


}
