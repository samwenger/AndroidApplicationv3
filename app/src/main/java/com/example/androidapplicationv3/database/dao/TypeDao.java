package com.example.androidapplicationv3.database.dao;

import android.database.sqlite.SQLiteConstraintException;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.androidapplicationv3.database.entity.TypeEntity;

@Dao
public interface TypeDao {

    @Insert
    long insert(TypeEntity type) throws SQLiteConstraintException;

    @Query("DELETE FROM types")
    void deleteAll();
}
