package com.example.androidapplicationv3.database.async.requests;

import android.app.Application;
import android.os.AsyncTask;

import com.example.androidapplicationv3.BaseApp;
import com.example.androidapplicationv3.database.entity.RequestEntity;
import com.example.androidapplicationv3.util.OnAsyncEventListener;

public class AddRequest extends AsyncTask<RequestEntity, Void, Void> {

    private Application application;
    private OnAsyncEventListener callback;
    private Exception exception;

    public AddRequest(Application application, OnAsyncEventListener callback) {
        this.application = application;
        this.callback = callback;
    }


    @Override
    protected Void doInBackground(RequestEntity... requestEntities) {
        try {
            for (RequestEntity request : requestEntities)
                ((BaseApp) application).getDatabase().requestDao()
                        .insert(request);
        } catch (Exception e) {
            exception = e;
        }
        return null;
    }

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
