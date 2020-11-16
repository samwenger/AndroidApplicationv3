package com.example.androidapplicationv3.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.androidapplicationv3.BaseApp;
import com.example.androidapplicationv3.database.async.requests.AddRequest;
import com.example.androidapplicationv3.database.async.requests.DeleteRequest;
import com.example.androidapplicationv3.database.async.requests.UpdateRequest;
import com.example.androidapplicationv3.database.entity.RequestEntity;
import com.example.androidapplicationv3.database.pojo.RequestWithType;
import com.example.androidapplicationv3.database.pojo.RequestWithUser;
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


    public LiveData<RequestWithUser> getRequestWithInfos(final Long id, Application application) {
        return ((BaseApp) application).getDatabase().requestDao().getWithInfos(id);
    }


    public LiveData<List<RequestWithType>> getRequestByUserWithInfos(final Long id, Application application) {
        return ((BaseApp) application).getDatabase().requestDao().getByIdUserWithInfos(id);
    }

    public LiveData<List<RequestWithUser>> getRequestByStatus(final Long id, Application application) {
        return ((BaseApp) application).getDatabase().requestDao().getByIdStatusWithInfos(id);
    }

    public void update(final RequestEntity request, OnAsyncEventListener callback,
                       Application application) {
        new UpdateRequest(application, callback).execute(request);
    }

    public void delete(final RequestEntity request, OnAsyncEventListener callback,
                       Application application) {
        new DeleteRequest(application, callback).execute(request);
    }
}
