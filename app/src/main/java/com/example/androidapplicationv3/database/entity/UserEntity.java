package com.example.androidapplicationv3.database.entity;

import androidx.room.Entity;

import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;


@Entity(tableName = "users", indices = {@Index(value = "login", unique = true)})
public class UserEntity {

    @PrimaryKey(autoGenerate = true)
    private Long idUser;

    private String lastname;

    private String firstname;

    private String login;

    private String password;

    private int remainingDays;

    private boolean isAdmin;


    public UserEntity() {
    }

    public UserEntity(String lastname, String firstname, String login, String password, int remainingDays, Boolean isAdmin){
        this.lastname = lastname;
        this.firstname = firstname;
        this.login = login;
        this.password = password;
        this.remainingDays = remainingDays;
        this.isAdmin = isAdmin;
    }


    public Long getIdUser() {
        return idUser;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public int getRemainingDays() {
        return remainingDays;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRemainingDays(int remainingDays) {
        this.remainingDays = remainingDays;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return that.getIdUser().equals(this.getIdUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, lastname, firstname, login, password, remainingDays);
    }
}
