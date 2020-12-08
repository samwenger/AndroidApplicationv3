package com.example.androidapplicationv3.ui.request;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
                intent.putExtra("userId", requests.get(position).request.getIdUser());
                startActivity(intent);
            }

        });

        RequestListViewModel.Factory factory = new RequestListViewModel.Factory(getApplication());
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