package com.example.androidapplicationv3.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapplicationv3.BaseApp;
import com.example.androidapplicationv3.database.pojo.RequestWithType;
import com.example.androidapplicationv3.database.repository.RequestRepository;

import java.util.List;

public class RequestListViewModel extends AndroidViewModel {

    private RequestRepository requestRepository;

    private final MediatorLiveData<List<RequestWithType>> observableRequests;

    public RequestListViewModel(@NonNull Application application,
                                RequestRepository requestRepository) {
        super(application);

        this.requestRepository = requestRepository;

        observableRequests = new MediatorLiveData<>();
        observableRequests.setValue(null);

        LiveData<List<RequestWithType>> requests = this.requestRepository.getCurrentUserRequests();

        observableRequests.addSource(requests, observableRequests::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final RequestRepository requestRepository;

        public Factory(@NonNull Application application) {
            this.application = application;
            requestRepository = ((BaseApp) application).getRequestRepository();
        }


        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new RequestListViewModel(application, requestRepository);
        }
    }

    /**
     * Expose the LiveData ClientAccounts query so the UI can observe it.
     */
    public LiveData<List<RequestWithType>> getRequestsWithInfosByUser() {
        return observableRequests;
    }


}

