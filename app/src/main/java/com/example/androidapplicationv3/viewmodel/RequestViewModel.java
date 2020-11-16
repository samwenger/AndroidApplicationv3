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

    private Application application;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<RequestWithUser> observableRequest;

    public RequestViewModel(@NonNull Application application,
                           final Long idRequest, RequestRepository requestRepository) {
        super(application);

        this.application = application;

        this.requestRepository = requestRepository;

        observableRequest = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableRequest.setValue(null);

        LiveData<RequestWithUser> request = requestRepository.getRequestWithInfos(idRequest, application);

        // observe the changes of the client entity from the database and forward them
        observableRequest.addSource(request, observableRequest::setValue);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final Long requestId;

        private final RequestRepository requestRepository;

        public Factory(@NonNull Application application, Long requestId) {
            this.application = application;
            this.requestId = requestId;
            requestRepository = ((BaseApp) application).getRequestRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new RequestViewModel(application, requestId, requestRepository);
        }
    }

    public LiveData<RequestWithUser> getRequest() {
        return observableRequest;
    }

    public void updateRequest(RequestEntity request, OnAsyncEventListener callback) {
        requestRepository.update(request, callback, application);
    }


    public void addRequest(RequestEntity request, OnAsyncEventListener callback) {
        requestRepository.insert(request, callback, application);
    }

    public void deleteRequest(RequestEntity request, OnAsyncEventListener callback) {
        requestRepository.delete(request, callback, application);
    }}


