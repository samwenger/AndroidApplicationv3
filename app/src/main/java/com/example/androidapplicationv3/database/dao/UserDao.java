package com.example.androidapplicationv3.database.dao;

import android.database.sqlite.SQLiteConstraintException;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.androidapplicationv3.database.entity.UserEntity;

@Dao
public interface UserDao {

    @Query("SELECT * FROM users WHERE login = :username")
    LiveData<UserEntity> getByUsername(String username);

    @Insert
    long insert(UserEntity user) throws SQLiteConstraintException;

    @Query("DELETE FROM users")
    void deleteAll();

}
