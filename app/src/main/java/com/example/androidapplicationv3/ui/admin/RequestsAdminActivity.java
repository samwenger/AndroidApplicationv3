package com.example.androidapplicationv3.ui.admin;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.androidapplicationv3.R;
import com.example.androidapplicationv3.adapter.RecyclerAdapterRequestsForAdmin;
import com.example.androidapplicationv3.adapter.RecyclerAdapterRequestsForUser;
import com.example.androidapplicationv3.database.pojo.RequestWithUser;
import com.example.androidapplicationv3.ui.BaseActivity;
import com.example.androidapplicationv3.util.RecyclerViewItemClickListener;
import com.example.androidapplicationv3.viewmodel.RequestAdminListViewModel;

import java.util.ArrayList;
import java.util.List;

public class RequestsAdminActivity extends BaseActivity {

    private List<RequestWithUser> requests;
    private RecyclerAdapterRequestsForAdmin<RequestWithUser> adapter;
    private RequestAdminListViewModel requestListViewModel;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // initialize view
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_requestsadmin, frameLayout);
        setTitle(getString(R.string.title_activity_adminrequests));
        navigationView.setCheckedItem(position);

        recyclerView = findViewById(R.id.adminRequestsRecyclerView);



        // configure linear layout manager for the recycler adapter
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);




        // Get data from the database
        adapter = new RecyclerAdapterRequestsForAdmin<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(RequestsAdminActivity.this, DetailsRequestAdminActivity.class);
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_NO_HISTORY
                );
                intent.putExtra("requestId", requests.get(position).request.getIdRequest());
                startActivity(intent);
            }
        });

        requests = new ArrayList<>();

        RequestAdminListViewModel.Factory factory = new RequestAdminListViewModel.Factory(getApplication(), new Long(1));
        requestListViewModel = ViewModelProviders.of(this,factory).get(RequestAdminListViewModel.class);
        requestListViewModel.getRequestByStatus().observe(this, requestEntities -> {
            if(requestEntities != null) {
                requests = requestEntities;
                adapter.setData(requests);
            }
        });

        recyclerView.setAdapter(adapter);
    }


    /**
     * Update navigation drawer on resume
     */
    @Override
    protected void onResume() {
        position = R.id.nav_adminrequests;
        navigationView.setCheckedItem(position);
        super.onResume();
    }
}