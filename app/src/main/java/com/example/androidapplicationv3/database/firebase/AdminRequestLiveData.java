package com.example.androidapplicationv3.database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.androidapplicationv3.database.entity.RequestEntity;
import com.example.androidapplicationv3.database.entity.UserEntity;
import com.example.androidapplicationv3.database.pojo.RequestWithType;
import com.example.androidapplicationv3.database.pojo.RequestWithUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
            setValue(toRequestWithUser(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    private RequestWithUser toRequestWithUser(DataSnapshot snapshot) {
        RequestWithUser requestWithUser = new RequestWithUser();

        requestWithUser.request = snapshot.getValue(RequestEntity.class);
        requestWithUser.request.setIdRequest(snapshot.getKey());
        requestWithUser.request.setIdUser(idUser);

        requestWithUser.type.setType(requestWithUser.request.getIdType());
        requestWithUser.status.setStatus(requestWithUser.request.getIdStatus());

      //  requestWithUser.user = ;
      //  requestWithUser.user.setIdUser(idUser);

        return requestWithUser;
    }



}
