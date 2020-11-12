package com.example.androidapplicationv3.ui.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.androidapplicationv3.R;
import com.example.androidapplicationv3.ui.BaseActivity;

public class RegisterUserActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_register_user, frameLayout);

        setTitle(getString(R.string.title_activity_registerUser));
        navigationView.setCheckedItem(position);

    }
}