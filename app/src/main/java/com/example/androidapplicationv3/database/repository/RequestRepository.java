package com.example.androidapplicationv3.database.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.androidapplicationv3.database.entity.RequestEntity;
import com.example.androidapplicationv3.database.entity.StatusEntity;
import com.example.androidapplicationv3.database.entity.TypeEntity;
import com.example.androidapplicationv3.database.firebase.AdminRequestListLiveData;
import com.example.androidapplicationv3.database.firebase.AdminRequestLiveData;
import com.example.androidapplicationv3.database.firebase.RequestListLiveData;
import com.example.androidapplicationv3.database.pojo.RequestWithType;
import com.example.androidapplicationv3.database.pojo.RequestWithUser;
import com.example.androidapplicationv3.util.OnAsyncEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestRepository {

    private static RequestRepository instance;

    private List<StatusEntity> statuses;
    private List<TypeEntity> types;


    private RequestRepository() {
       statuses = getStatuses();
       types = getTypes();
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



    public List<StatusEntity> getStatuses() {
        List<StatusEntity> statuses = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("statuses");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot childSnapshot : snapshot.getChildren()) {
                    StatusEntity status = childSnapshot.getValue(StatusEntity.class);
                    status.setIdStatus(childSnapshot.getKey());
                    statuses.add(status);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return statuses;

    }

    public StatusEntity getStatusById(String idStatus) {
        for(StatusEntity status : statuses) {
            if(status.getIdStatus().equals(idStatus))
                return status;
        }
        return null;
    }


    public List<TypeEntity> getTypes() {
        List<TypeEntity> types = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("types");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot childSnapshot : snapshot.getChildren()) {
                    TypeEntity type = childSnapshot.getValue(TypeEntity.class);
                    type.setIdType(childSnapshot.getKey());
                    types.add(type);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return types;

    }

    public TypeEntity getTypeById(String idType) {

        for(TypeEntity type : types) {
            if(type.getIdType().equals(idType))
                return type;
        }
        return null;

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
