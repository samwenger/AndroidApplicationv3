package com.example.androidapplicationv3;

import android.app.Application;

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

    public UserRepository getUserRepository() {
        return UserRepository.getInstance();
    }

    public RequestRepository getRequestRepository() {
        return RequestRepository.getInstance();
    }

}