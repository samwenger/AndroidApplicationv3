package com.example.androidapplicationv3.database.firebase;

import androidx.lifecycle.LiveData;

import com.example.androidapplicationv3.database.pojo.RequestWithUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class AdminRequestListLiveData extends LiveData<List<RequestWithUser>> {

    private static final String TAG = "AdminRequestListLiveData";

    private final DatabaseReference reference;
    private final String idStatus;
   // private final AdminRequestListLiveData.MyValueEventListener listener =
     //       new AdminRequestListLiveData.MyValueEventListener();

    public AdminRequestListLiveData(DatabaseReference ref, String idStatus) {
        reference = ref;
        this.idStatus = idStatus;
    }
}
