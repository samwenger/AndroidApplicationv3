package com.example.androidapplicationv3.ui.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.lifecycle.ViewModelProviders;

import com.example.androidapplicationv3.R;

import com.example.androidapplicationv3.database.pojo.RequestWithUser;
import com.example.androidapplicationv3.ui.BaseActivity;
import com.example.androidapplicationv3.util.OnAsyncEventListener;
import com.example.androidapplicationv3.viewmodel.RequestViewModel;

import java.text.SimpleDateFormat;

public class DetailsRequestAdminActivity extends BaseActivity {

    private TextView inputUser;
    private TextView inputType;
    private TextView inputStatus;
    private TextView inputDateStart;
    private TextView inputDateEnd;
    private TextView inputRemarks;
    private ImageButton buttonAccept;
    private ImageButton buttonDeny;

    private RequestViewModel viewModel;
    private RequestWithUser request;

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // initialize view
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_detailsrequestadmin, frameLayout);
        setTitle(getString(R.string.title_activity_detailsrequestadmin));
        initiateView();

        // add a listener on buttons
        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStatus(new Long(2));

            }
        });

        buttonDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStatus(new Long(3));
            }
        });

        // get the id of the request to show
        Long requestId = getIntent().getLongExtra("requestId", 0);

        // get LiveData
        RequestViewModel.Factory factory = new RequestViewModel.Factory(getApplication(), requestId);
        viewModel = ViewModelProviders.of(this, factory).get(RequestViewModel.class);
        viewModel.getRequest().observe(this, requestEntity -> {
            if (requestEntity != null) {
                request = requestEntity;
                updateContent();
            }
        });

    }


    /**
     * Initialize view
     */
    private void initiateView() {
        inputUser = findViewById(R.id.adminInputUser);
        inputType = findViewById(R.id.adminInputType);
        inputStatus = findViewById(R.id.adminInputStatus);
        inputDateStart = findViewById(R.id.adminInputDateStart);
        inputDateEnd = findViewById(R.id.adminInputDateEnd);
        inputRemarks = findViewById(R.id.adminInputRemarks);
        buttonAccept = findViewById(R.id.button_accept);
        buttonDeny = findViewById(R.id.button_deny);
    }


    /**
     * Set text values once the data has been retrieved from database
     */
    private void updateContent() {
        if (request != null) {

            String dateDebut = new SimpleDateFormat("dd/MM/yyyy").format(request.request.getDateDebut());
            String dateFin = new SimpleDateFormat("dd/MM/yyyy").format(request.request.getDateFin());

            inputUser.setText(request.user.getFirstname()+" "+request.user.getLastname());
            inputType.setText(request.type.getType());
            inputStatus.setText(request.status.getStatus());
            inputDateStart.setText(dateDebut);
            inputDateEnd.setText(dateFin);
            inputRemarks.setText(request.request.getRemark());
        }
    }


    /**
     * Update request's status when admin click on validate or deny
     * @param id
     * @return
     */
    private boolean saveStatus(Long id){

        request.request.setIdStatus(id);

        viewModel.updateRequest(request.request, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                setResponse(true);
                onBackPressed();
            }

            @Override
            public void onFailure(Exception e) {
                setResponse(false);
            }
        });
        return true;
    }


    /**
     * Message to user to confirm the result of the action
     * @param response
     */
    private void setResponse(Boolean response) {
        if (response) {
            updateContent();
            toast = Toast.makeText(this, getString(R.string.request_edited_msg), Toast.LENGTH_LONG);
            toast.show();
        } else {
            toast = Toast.makeText(this, getString(R.string.error_request_notedited), Toast.LENGTH_LONG);
        }
    }

}
