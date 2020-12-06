package com.example.androidapplicationv3.database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.androidapplicationv3.database.entity.RequestEntity;
import com.example.androidapplicationv3.database.pojo.RequestWithType;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class RequestLiveData extends LiveData<RequestWithType> {

    private static final String TAG = "RequestLiveData";

    private final DatabaseReference reference;
    private final String idUser;
    private final RequestLiveData.MyValueEventListener listener = new RequestLiveData.MyValueEventListener();

    public RequestLiveData(DatabaseReference ref) {
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

            RequestWithType requestWithType = new RequestWithType();

            requestWithType.request = dataSnapshot.getValue(RequestEntity.class);
            requestWithType.request.setIdRequest(dataSnapshot.getKey());
            requestWithType.request.setIdUser(idUser);

            requestWithType.status.setStatus(requestWithType.request.getIdStatus());
            requestWithType.type.setType(requestWithType.request.getIdType());

            setValue(requestWithType);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

}
