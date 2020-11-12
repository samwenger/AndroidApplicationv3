package com.example.androidapplicationv3.database.async.users;

import android.app.Application;
import android.os.AsyncTask;

import com.example.androidapplicationv3.BaseApp;
import com.example.androidapplicationv3.database.entity.UserEntity;
import com.example.androidapplicationv3.util.OnAsyncEventListener;

public class AddUser extends AsyncTask<UserEntity, Void, Void> {

    private Application application;
    private OnAsyncEventListener callback;
    private Exception exception;

    public AddUser(Application application, OnAsyncEventListener callback) {
        this.application = application;
        this.callback = callback;
    }


    @Override
    protected Void doInBackground(UserEntity... userEntities) {
        try {
            for (UserEntity user : userEntities)
                ((BaseApp) application).getDatabase().userDao()
                        .insert(user);
        } catch (Exception e) {
            exception = e;
        }
        return null;    }


    @Override
    protected void onPostExecute(Void aVoid) {
        if (callback != null) {
            if (exception == null) {
                callback.onSuccess();
            } else {
                callback.onFailure(exception);
            }
        }
    }
}
