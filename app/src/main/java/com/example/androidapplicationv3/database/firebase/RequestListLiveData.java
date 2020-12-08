package com.example.androidapplicationv3.database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.androidapplicationv3.database.entity.RequestEntity;
import com.example.androidapplicationv3.database.pojo.RequestWithType;
import com.example.androidapplicationv3.database.repository.RequestRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestListLiveData extends LiveData<List<RequestWithType>> {

    private static final String TAG = "RequestListLiveData";

    private final DatabaseReference reference;
    private final String idUser;
    private final RequestListLiveData.MyValueEventListener listener =
            new RequestListLiveData.MyValueEventListener();

    public RequestListLiveData(DatabaseReference ref) {
        reference = ref;
        this.idUser = ref.getKey();
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
            setValue(toRequestsWithInfos(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }


    private List<RequestWithType> toRequestsWithInfos(DataSnapshot snapshot) {
        List<RequestWithType> requests = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            RequestWithType requestWithType = new RequestWithType();
            requestWithType.request = childSnapshot.getValue(RequestEntity.class);
            requestWithType.request.setIdRequest(childSnapshot.getKey());
            requestWithType.request.setIdUser(idUser);

            requestWithType.status = RequestRepository.getInstance().getStatusById(requestWithType.request.getIdStatus());
            requestWithType.type = RequestRepository.getInstance().getTypeById(requestWithType.request.getIdType());

            requests.add(requestWithType);

        }
        return requests;
    }

}
