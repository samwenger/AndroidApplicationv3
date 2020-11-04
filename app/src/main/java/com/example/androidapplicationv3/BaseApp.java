package com.example.androidapplicationv3;

import android.app.Application;
import android.app.DownloadManager;

import com.example.androidapplicationv3.database.AppDatabase;
import com.example.androidapplicationv3.database.repository.RequestRepository;
import com.example.androidapplicationv3.database.repository.UserRepository;

/**
 * Android Application class. Used for accessing singletons.
 */
public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this);
    }

    public UserRepository getUserRepository() {
        return UserRepository.getInstance();
    }

    public RequestRepository getRequestRepository() {
        return RequestRepository.getInstance();
    }

}