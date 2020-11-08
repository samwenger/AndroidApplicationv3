package com.example.androidapplicationv3.ui.request;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.androidapplicationv3.BaseApp;
import com.example.androidapplicationv3.R;
import com.example.androidapplicationv3.database.entity.RequestEntity;
import com.example.androidapplicationv3.database.pojo.RequestWithType;
import com.example.androidapplicationv3.ui.BaseActivity;
import com.example.androidapplicationv3.ui.MainActivity;
import com.example.androidapplicationv3.viewmodel.RequestListViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestsActivity extends BaseActivity {

    private List<RequestWithType> requests;
    private List<Map<String, String>> requestsToDisplay;
    private ArrayAdapter adapter;
    private RequestListViewModel requestListViewModel;
    private ListView requestsListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_requests, frameLayout);

        setTitle(getString(R.string.title_activity_requests));
        navigationView.setCheckedItem(position);

        requestsListView = findViewById(R.id.requestsListView);

        // Get userId
        SharedPreferences settings = getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        Long idUser = settings.getLong(BaseActivity.PREFS_IDUSER, 0);

        requests = new ArrayList<>();
        requestsToDisplay = new ArrayList<Map<String, String>>();

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, requests);


        requestsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RequestsActivity.this, DetailsRequestActivity.class);
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_NO_HISTORY
                );
                intent.putExtra("requestId", requests.get(position).request.getIdRequest());
                startActivity(intent);
            }
        });


        // Get data from the database
        RequestListViewModel.Factory factory = new RequestListViewModel.Factory(getApplication(), idUser);
        requestListViewModel = ViewModelProviders.of(this,factory).get(RequestListViewModel.class);
        requestListViewModel.getRequestsByUser().observe(this, requestEntities -> {
            if(requestEntities != null) {
                requests = requestEntities;
                adapter.addAll(requests);
            }
        });

        requestsListView.setAdapter(adapter);
    }
}