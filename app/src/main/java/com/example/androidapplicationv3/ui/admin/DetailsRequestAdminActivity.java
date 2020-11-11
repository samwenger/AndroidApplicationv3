package com.example.androidapplicationv3.ui.admin;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;

import com.example.androidapplicationv3.R;
import com.example.androidapplicationv3.database.converters.Converters;
import com.example.androidapplicationv3.database.pojo.RequestWithType;
import com.example.androidapplicationv3.ui.BaseActivity;
import com.example.androidapplicationv3.util.OnAsyncEventListener;
import com.example.androidapplicationv3.viewmodel.RequestViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DetailsRequestAdminActivity extends BaseActivity {

    private TextView inputType;
    private TextView inputStatus;
    private TextView inputDateStart;
    private TextView inputDateEnd;
    private EditText inputRemarks;

    private RequestViewModel viewModel;
    private RequestWithType request;

    private Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_detailsrequestadmin, frameLayout);

        setTitle(getString(R.string.title_activity_detailsrequestadmin));

        initiateView();

        Long requestId = getIntent().getLongExtra("requestId", 0);


        RequestViewModel.Factory factory = new RequestViewModel.Factory(getApplication(), requestId);
        viewModel = ViewModelProviders.of(this, factory).get(RequestViewModel.class);
        viewModel.getRequest().observe(this, requestEntity -> {
            if (requestEntity != null) {
                request = requestEntity;
                updateContent();
            }
        });

    }

    private void initiateView() {
        inputType = findViewById(R.id.inputType);
        inputStatus = findViewById(R.id.inputStatus);
        inputDateStart = findViewById(R.id.inputDateStart);
        inputDateEnd = findViewById(R.id.inputDateEnd);
        inputRemarks = findViewById(R.id.inputRemarks);
    }

    private void updateContent() {
        if (request != null) {

            String dateDebut = new SimpleDateFormat("dd/MM/yyyy").format(request.request.getDateDebut());
            String dateFin = new SimpleDateFormat("dd/MM/yyyy").format(request.request.getDateFin());

            inputType.setText(request.type.getType());
            inputStatus.setText(request.status.getStatus());
            inputDateStart.setText(dateDebut);
            inputDateEnd.setText(dateFin);
            inputRemarks.setText(request.request.getRemark());
        }
    }

}
