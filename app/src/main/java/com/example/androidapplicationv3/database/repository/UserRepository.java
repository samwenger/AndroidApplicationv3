package com.example.androidapplicationv3.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.androidapplicationv3.BaseApp;
import com.example.androidapplicationv3.database.entity.UserEntity;

public class UserRepository {

    private static UserRepository instance;

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            synchronized (UserRepository.class) {
                if (instance == null) {
                    instance = new UserRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<UserEntity> getUser(final String username, Application application) {
        return ((BaseApp) application).getDatabase().userDao().getByUsername(username);
    }
}
