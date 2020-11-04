package com.example.androidapplicationv3.ui.request;

import androidx.core.view.GravityCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.androidapplicationv3.BaseApp;
import com.example.androidapplicationv3.R;
import com.example.androidapplicationv3.database.async.requests.AddRequest;
import com.example.androidapplicationv3.database.converters.Converters;
import com.example.androidapplicationv3.database.entity.RequestEntity;
import com.example.androidapplicationv3.database.repository.RequestRepository;
import com.example.androidapplicationv3.ui.BaseActivity;
import com.example.androidapplicationv3.util.OnAsyncEventListener;
import com.example.androidapplicationv3.viewmodel.RequestViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddRequestActivity extends BaseActivity {

    private static final String TAG = "AddRequestActivity";

    private Converters converters = new Converters();
    private RequestViewModel viewModel;

    private RequestRepository requestRepository;
    private RadioGroup radioButtonGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;

    private EditText startDate;
    private EditText endDate;
    private EditText remarks;
    private Long idUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_add_request, frameLayout);

        setTitle(getString(R.string.title_activity_addrequest));
        navigationView.setCheckedItem(position);

        requestRepository = ((BaseApp) getApplication()).getRequestRepository();

        radioButtonGroup = findViewById(R.id.radioButtonGroupAddRequest);
        radioButton1 = findViewById(R.id.radioButtonVacationAddRequest);
        radioButton2 = findViewById(R.id.radioButtonVacationAddRequest);
        radioButton3 = findViewById(R.id.radioButtonVacationAddRequest);
        radioButton4 = findViewById(R.id.radioButtonVacationAddRequest);


        startDate = findViewById(R.id.startDateAddRequest);
        endDate = findViewById(R.id.endDateAddRequest);
        remarks = findViewById(R.id.remarksAddRequest);

        SharedPreferences settings = getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        idUser = settings.getLong(BaseActivity.PREFS_IDUSER, 0);

        Button button = findViewById(R.id.buttonAddRequest);
        button.setOnClickListener(view -> {
            try {
                addRequest();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

    }


    private void addRequest() throws ParseException {

        RequestEntity newRequest = new RequestEntity();
        newRequest.setIdUser(idUser);


        Date dateDebut = new SimpleDateFormat("dd/MM/yyyy").parse(startDate.getText().toString());
        Date dateFin = new SimpleDateFormat("dd/MM/yyyy").parse(endDate.getText().toString());

        newRequest.setDateDebut(converters.dateToTimestamp(dateDebut));
        newRequest.setDateFin(converters.dateToTimestamp(dateFin));
        newRequest.setRemark(remarks.getText().toString());
        newRequest.setIdStatus(new Long(1));


        int selectedId = radioButtonGroup.getCheckedRadioButtonId();
        Long typeId = null;

        if(selectedId == radioButton1.getId()){
            typeId = new Long(1);
        } else if (selectedId == radioButton2.getId()) {
            typeId = new Long(2);
        } else if (selectedId == radioButton3.getId()) {
            typeId = new Long(3);
        } else if (selectedId == radioButton4.getId()) {
            typeId = new Long(4);
        }
        newRequest.setIdType(typeId);



        new AddRequest(getApplication(), new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "createUserWithEmail: success");
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "createUserWithEmail: failure", e);
            }
        }).execute(newRequest);

        /*
        viewModel.addRequest(newRequest, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "AddRequest: success");
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "AddRequest: failure", e);
            }
        });*/
    }

}
