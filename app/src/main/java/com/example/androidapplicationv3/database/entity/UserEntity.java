package com.example.androidapplicationv3.database.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class UserEntity {

    private String idUser;

    private String lastname;

    private String firstname;

    private String login;

    private String password;

    private boolean isAdmin;


    public UserEntity() {
    }

    public UserEntity(String lastname, String firstname, String login, String password, Boolean isAdmin){
        this.lastname = lastname;
        this.firstname = firstname;
        this.login = login;
        this.password = password;
        this.isAdmin = isAdmin;
    }


    @Exclude
    public String getIdUser() {
        return idUser;
    }

    public String getLastname() {
        return lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    @Exclude
    public String getLogin() {
        return login;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIdUser(String idUser) {
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
        return Objects.hash(idUser, lastname, firstname, login, password);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("lastname", lastname);
        result.put("firstname", firstname);
        result.put("isAdmin", isAdmin);

        return result;
    }

}
