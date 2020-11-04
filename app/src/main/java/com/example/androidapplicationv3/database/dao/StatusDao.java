package com.example.androidapplicationv3.database.dao;

import android.database.sqlite.SQLiteConstraintException;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.androidapplicationv3.database.entity.StatusEntity;

@Dao
public interface StatusDao {

    @Insert
    long insert(StatusEntity status) throws SQLiteConstraintException;

    @Query("DELETE FROM status")
    void deleteAll();
}
