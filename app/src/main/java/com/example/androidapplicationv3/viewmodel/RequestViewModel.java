package com.example.androidapplicationv3.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapplicationv3.BaseApp;
import com.example.androidapplicationv3.database.entity.RequestEntity;
import com.example.androidapplicationv3.database.pojo.RequestWithUser;
import com.example.androidapplicationv3.database.repository.RequestRepository;
import com.example.androidapplicationv3.util.OnAsyncEventListener;

public class RequestViewModel extends AndroidViewModel {

    private RequestRepository requestRepository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<RequestWithUser> observableRequest;

    public RequestViewModel(@NonNull Application application,
                           final String idRequest, final String idUser, RequestRepository requestRepository) {
        super(application);

        this.requestRepository = requestRepository;

        observableRequest = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableRequest.setValue(null);

        LiveData<RequestWithUser> request = requestRepository.getRequest(idUser, idRequest);

        // observe the changes of the client entity from the database and forward them
        observableRequest.addSource(request, observableRequest::setValue);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final String requestId;
        private final String userId;

        private final RequestRepository requestRepository;

        public Factory(@NonNull Application application, String requestId, String userId) {
            this.application = application;
            this.requestId = requestId;
            this.userId = userId;
            requestRepository = ((BaseApp) application).getRequestRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new RequestViewModel(application, requestId, userId, requestRepository);
        }
    }

    public LiveData<RequestWithUser> getRequest() {
        return observableRequest;
    }

    public void updateRequest(RequestEntity request, OnAsyncEventListener callback) {
        requestRepository.update(request, callback);
    }

    public void deleteRequest(RequestEntity request, OnAsyncEventListener callback) {
        requestRepository.delete(request, callback);
    }}


