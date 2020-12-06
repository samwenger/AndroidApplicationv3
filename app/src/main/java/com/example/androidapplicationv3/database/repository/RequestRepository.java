package com.example.androidapplicationv3.database.repository;

import androidx.lifecycle.LiveData;

import com.example.androidapplicationv3.database.entity.RequestEntity;
import com.example.androidapplicationv3.database.firebase.AdminRequestListLiveData;
import com.example.androidapplicationv3.database.firebase.AdminRequestLiveData;
import com.example.androidapplicationv3.database.firebase.RequestListLiveData;
import com.example.androidapplicationv3.database.firebase.RequestLiveData;
import com.example.androidapplicationv3.database.pojo.RequestWithType;
import com.example.androidapplicationv3.database.pojo.RequestWithUser;
import com.example.androidapplicationv3.util.OnAsyncEventListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    public void insert(final RequestEntity request, final OnAsyncEventListener callback) {

        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("requests")
                .child(request.getIdUser());
        String key = reference.push().getKey();
        FirebaseDatabase.getInstance()
                .getReference("requests")
                .child(request.getIdUser())
                .child(key)
                .setValue(request, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }


    public LiveData<List<RequestWithType>> getCurrentUserRequests() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("requests")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        return new RequestListLiveData(reference);
    }


    public LiveData<List<RequestWithUser>> getAllRequestsWithUser(final String idStatus) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("requests");
        return new AdminRequestListLiveData(reference, idStatus);
    }

    public LiveData<RequestWithUser> getRequest(final String userId, final String requestId) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("requests")
                .child(userId)
                .child(requestId);
        return new AdminRequestLiveData(reference);
    }


    public void update(final RequestEntity request, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("requests")
                .child(request.getIdUser())
                .child(request.getIdRequest())
                .updateChildren(request.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }


    public void delete(final RequestEntity request, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference("requests")
                .child(request.getIdUser())
                .child(request.getIdRequest())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}
