package com.example.androidapplicationv3.database.dao;

import android.database.sqlite.SQLiteConstraintException;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.androidapplicationv3.database.entity.RequestEntity;
import com.example.androidapplicationv3.database.entity.UserEntity;
import com.example.androidapplicationv3.database.pojo.RequestWithType;

import java.util.List;

@Dao
public interface RequestDao {
    @Insert
    long insert(RequestEntity request) throws SQLiteConstraintException;

    @Query("SELECT * FROM requests WHERE idRequest = :id")
    LiveData<RequestEntity> getById(Long id);

    @Query("SELECT * FROM requests WHERE idUser = :id")
    LiveData<List<RequestEntity>> getByIdUser(Long id);

    @Query("SELECT * FROM requests WHERE idRequest = :id")
    LiveData<RequestWithType> getWithInfos(Long id);

    @Query("SELECT * FROM requests WHERE idUser = :id")
    LiveData<List<RequestWithType>> getByIdUserWithInfos(Long id);

    @Update
    public abstract void update(RequestEntity request);

    @Delete
    void delete(RequestEntity request);

}
