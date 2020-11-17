package com.example.androidapplicationv3.ui.request;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.androidapplicationv3.R;
import com.example.androidapplicationv3.adapter.RecyclerAdapterRequestsForUser;
import com.example.androidapplicationv3.database.pojo.RequestWithType;
import com.example.androidapplicationv3.ui.BaseActivity;
import com.example.androidapplicationv3.util.RecyclerViewItemClickListener;
import com.example.androidapplicationv3.viewmodel.RequestListViewModel;

import java.util.ArrayList;
import java.util.List;

public class RequestsActivity extends BaseActivity {

    private List<RequestWithType> requests;
    private RecyclerAdapterRequestsForUser<RequestWithType> adapter;
    private RequestListViewModel requestListViewModel;
    private RecyclerView requestsListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // initialize view
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_requests, frameLayout);
        setTitle(getString(R.string.title_activity_requests));
        navigationView.setCheckedItem(position);

        requestsListView = findViewById(R.id.requestsListView);

        //mange linear layout manager for the recycler view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        requestsListView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requestsListView.getContext(),
                LinearLayoutManager.VERTICAL);
        requestsListView.addItemDecoration(dividerItemDecoration);


        // Get the id of the current user
        SharedPreferences settings = getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        Long idUser = settings.getLong(BaseActivity.PREFS_IDUSER, 0);


        // Get data from the database
        requests = new ArrayList<>();
        adapter = new RecyclerAdapterRequestsForUser<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(RequestsActivity.this, DetailsRequestActivity.class);
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_NO_HISTORY
                );
                intent.putExtra("requestId", requests.get(position).request.getIdRequest());
                startActivity(intent);
            }

        });

        RequestListViewModel.Factory factory = new RequestListViewModel.Factory(getApplication(), idUser);
        requestListViewModel = ViewModelProviders.of(this,factory).get(RequestListViewModel.class);
        requestListViewModel.getRequestsWithInfosByUser().observe(this, requestEntities -> {
            if(requestEntities != null) {
                requests = requestEntities;
                adapter.setData(requests);
            }
        });

        requestsListView.setAdapter(adapter);
    }

    /**
     * Update navigation drawer onresume
     */
    @Override
    protected void onResume() {
        position = R.id.nav_requests;
        navigationView.setCheckedItem(position);
        super.onResume();
    }
}