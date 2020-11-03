package com.example.androidapplicationv3.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.androidapplicationv3.database.converters.Converters;

import java.sql.Date;
import java.util.Objects;


@Entity(tableName = "users")
public class UserEntity {

    @PrimaryKey(autoGenerate = true)
    private Long idUser;

    private String lastname;

    private String firstname;

    private String login;

    private String password;

    private Long birthday;

    private int remainingDays;


    public UserEntity(String lastname, String firstname, String login, String password, Long birthday, int remainingDays){
        this.lastname = lastname;
        this.firstname = firstname;
        this.login = login;
        this.password = password;
        this.birthday = birthday;
        this.remainingDays = remainingDays;
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

    public Long getBirthday() {
        return birthday;
    }

    public int getRemainingDays() {
        return remainingDays;
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

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public void setRemainingDays(int remainingDays) {
        this.remainingDays = remainingDays;
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
        return Objects.hash(idUser, lastname, firstname, login, password, birthday, remainingDays);
    }
}
