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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class AdminRequestLiveData extends LiveData<RequestWithUser> {

    private static final String TAG = "AdminRequestLiveData";

    private final DatabaseReference reference;
    private final String idUser;
    private final AdminRequestLiveData.MyValueEventListener listener =
            new AdminRequestLiveData.MyValueEventListener();

    public AdminRequestLiveData(DatabaseReference ref) {
        reference = ref;
        idUser = ref.getParent().getKey();
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

            if (!dataSnapshot.exists()) {
                return;
            }

            RequestWithUser requestWithUser = new RequestWithUser();

            LiveData<UserEntity> user = UserRepository.getInstance().getUser(idUser);
            user.observeForever(userEntity -> {
                if (userEntity != null) {
                    requestWithUser.request = dataSnapshot.getValue(RequestEntity.class);
                    requestWithUser.request.setIdRequest(dataSnapshot.getKey());
                    requestWithUser.request.setIdUser(idUser);

                    requestWithUser.status = RequestRepository.getInstance().getStatusById(requestWithUser.request.getIdStatus());
                    requestWithUser.type = RequestRepository.getInstance().getTypeById(requestWithUser.request.getIdType());
                    requestWithUser.user = userEntity;

                    setValue(requestWithUser);
                }
            });
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }
}
