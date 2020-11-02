package com.example.androidapplicationv3.database.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "user-role",
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
    private Long idUser;
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
