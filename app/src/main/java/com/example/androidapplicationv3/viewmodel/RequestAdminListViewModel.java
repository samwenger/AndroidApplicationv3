package com.example.androidapplicationv3.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapplicationv3.BaseApp;
import com.example.androidapplicationv3.database.pojo.RequestWithUser;
import com.example.androidapplicationv3.database.repository.RequestRepository;

import java.util.List;

public class RequestAdminListViewModel extends AndroidViewModel {

    private Application application;

    private RequestRepository requestRepository;

    private final MediatorLiveData<List<RequestWithUser>> observableRequests;

    public RequestAdminListViewModel(@NonNull Application application,
                                final Long idStatus,
                                RequestRepository requestRepository) {
        super(application);

        this.application = application;

        this.requestRepository = requestRepository;

        observableRequests = new MediatorLiveData<>();
        observableRequests.setValue(null);

        LiveData<List<RequestWithUser>> requests = this.requestRepository.getRequestByStatus(idStatus,application);

        observableRequests.addSource(requests, observableRequests::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final Long idStatus;

        private final RequestRepository requestRepository;

        public Factory(@NonNull Application application, Long idStatus) {
            this.application = application;
            this.idStatus = idStatus;
            requestRepository = ((BaseApp) application).getRequestRepository();
        }


        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new RequestAdminListViewModel(application, idStatus, requestRepository);
        }
    }

    /**
     * Expose the LiveData ClientAccounts query so the UI can observe it.
     */
    public LiveData<List<RequestWithUser>> getRequestByStatus() {
        return observableRequests;
    }


}

