package com.example.androidapplicationv3.ui.request;

import androidx.core.view.GravityCompat;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.androidapplicationv3.R;
import com.example.androidapplicationv3.ui.BaseActivity;


public class AddRequestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_add_request, frameLayout);

        setTitle(getString(R.string.title_activity_addrequest));
        navigationView.setCheckedItem(position);
    }


}