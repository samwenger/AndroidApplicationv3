package com.example.androidapplicationv3.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.androidapplicationv3.BaseApp;
import com.example.androidapplicationv3.database.async.requests.AddRequest;
import com.example.androidapplicationv3.database.entity.RequestEntity;
import com.example.androidapplicationv3.database.entity.UserEntity;
import com.example.androidapplicationv3.util.OnAsyncEventListener;

import java.util.List;

public class RequestRepository {

    private static RequestRepository instance;

    private RequestRepository() {
    }

    public static RequestRepository getInstance() {
        if (instance == null) {
            synchronized (RequestRepository.class) {
                if (instance == null) {
                    instance = new RequestRepository();
                }
            }
        }
        return instance;
    }

    public void insert(final RequestEntity request, OnAsyncEventListener callback,
                       Application application) {
        new AddRequest(application, callback).execute(request);
    }

    public LiveData<RequestEntity> getRequest(final Long id, Application application) {
        return ((BaseApp) application).getDatabase().requestDao().getById(id);
    }

    public LiveData<List<RequestEntity>> getRequestByUser(final Long id, Application application) {
        return ((BaseApp) application).getDatabase().requestDao().getByIdUser(id);
    }
}
