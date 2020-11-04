package com.example.androidapplicationv3.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.androidapplicationv3.database.entity.RequestEntity;
import com.example.androidapplicationv3.database.repository.RequestRepository;
import com.example.androidapplicationv3.util.OnAsyncEventListener;

public class RequestViewModel extends AndroidViewModel {

    private RequestRepository requestRepository;

    private Application application;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<RequestEntity> observableRequest;

    public RequestViewModel(@NonNull Application application,
                           final Long idRequest, RequestRepository requestRepository) {
        super(application);

        this.application = application;

        this.requestRepository = requestRepository;

        observableRequest = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableRequest.setValue(null);

        LiveData<RequestEntity> request = requestRepository.getRequest(idRequest, application);

        // observe the changes of the client entity from the database and forward them
        observableRequest.addSource(request, observableRequest::setValue);
    }

    public void addRequest(RequestEntity request, OnAsyncEventListener callback) {
        requestRepository.insert(request, callback, application);
    }
}


