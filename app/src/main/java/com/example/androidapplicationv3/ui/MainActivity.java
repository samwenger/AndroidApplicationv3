package com.example.androidapplicationv3.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.androidapplicationv3.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);

        setTitle(getString(R.string.title_activity_main));
        navigationView.setCheckedItem(position);
    }
}