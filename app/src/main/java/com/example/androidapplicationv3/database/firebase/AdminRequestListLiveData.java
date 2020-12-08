package com.example.androidapplicationv3.database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.androidapplicationv3.database.entity.RequestEntity;
import com.example.androidapplicationv3.database.entity.UserEntity;
import com.example.androidapplicationv3.database.pojo.RequestWithUser;
import com.example.androidapplicationv3.database.repository.RequestRepository;
import com.example.androidapplicationv3.database.repository.UserRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminRequestListLiveData extends LiveData<List<RequestWithUser>> {

    private static final String TAG = "AdminRequestListLiveData";

    private final Query reference;
    private final String idStatus;
    private final AdminRequestListLiveData.MyValueEventListener listener =
            new AdminRequestListLiveData.MyValueEventListener();

    public AdminRequestListLiveData(Query ref, String idStatus) {
        reference = ref;
        this.idStatus = idStatus;
    }

    @Override
    protected void onActive() {
        Log.d(TAG, "onActive");
        reference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        Log.d(TAG, "onInactive");
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<RequestWithUser> requestsWithUser = new ArrayList<>();
            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                String idUser = userSnapshot.getKey();

                LiveData<UserEntity> user = UserRepository.getInstance().getUser(idUser);
                user.observeForever(userEntity -> {
                    if (userEntity != null) {
                        for(DataSnapshot requestSnapshot : userSnapshot.getChildren()) {

                            RequestWithUser requestWithUser = new RequestWithUser();

                            requestWithUser.request = requestSnapshot.getValue(RequestEntity.class);
                            requestWithUser.request.setIdRequest(requestSnapshot.getKey());
                            requestWithUser.request.setIdUser(idUser);

                            requestWithUser.status = RequestRepository.getInstance().getStatusById(requestWithUser.request.getIdStatus());
                            requestWithUser.type = RequestRepository.getInstance().getTypeById(requestWithUser.request.getIdType());
                            requestWithUser.user = userEntity;

                            requestsWithUser.add(requestWithUser);
                            setValue(requestsWithUser);
                        }
                    }
                });
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }
}
