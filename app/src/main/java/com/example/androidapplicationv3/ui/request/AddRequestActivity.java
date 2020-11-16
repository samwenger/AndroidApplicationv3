package com.example.androidapplicationv3.ui.request;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AddRequestActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "AddRequestActivity";
    private static String SELECTED;

    private Converters converters = new Converters();
    private RequestViewModel viewModel;

    private RequestRepository requestRepository;
    private RadioGroup radioButtonGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;

    private TextView startDate;
    private TextView endDate;
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
        radioButton2 = findViewById(R.id.radioButtonSpecialLeaveAddRequest);
        radioButton3 = findViewById(R.id.radioButtonOvertimeAddRequest);
        radioButton4 = findViewById(R.id.radioButtonWithoutPayAddRequest);


        startDate = findViewById(R.id.inputDateStart);
        endDate = findViewById(R.id.inputDateEnd);
        remarks = findViewById(R.id.inputRemarks);


        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SELECTED = "STARTDATE";
                showDatePicketDialog();

            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SELECTED = "ENDDATE";
                showDatePicketDialog();
            }
        });

        SharedPreferences settings = getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        idUser = settings.getLong(BaseActivity.PREFS_IDUSER, 0);

        Button button = findViewById(R.id.buttonDeleteRequest);
        button.setOnClickListener(view -> {
            try {
                if(addRequest())
                    onBackPressed();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

    }



    private boolean addRequest() throws ParseException {

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
        } else {
            Toast.makeText(this, getString(R.string.error_addrequest_notype), Toast.LENGTH_LONG).show();
            return false;
        }


        Pattern datePattern = Pattern.compile("^([0-2]?[0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$");

        Matcher matcherStartDate = datePattern.matcher(startDate.getText().toString());
        boolean startDateMatching = matcherStartDate.matches();

        Matcher matcherEndDate = datePattern.matcher(endDate.getText().toString());
        boolean endDateMatching = matcherEndDate.matches();

        if (!startDateMatching){
            Toast.makeText(this, getString(R.string.error_addrequest_nostartdate), Toast.LENGTH_LONG).show();
            return false;
        } else if (!endDateMatching) {
            Toast.makeText(this, getString(R.string.error_addrequest_noenddate), Toast.LENGTH_LONG).show();
            return false;
        }

        Date dateDebut = new SimpleDateFormat("dd/MM/yyyy").parse(startDate.getText().toString());
        Date dateFin = new SimpleDateFormat("dd/MM/yyyy").parse(endDate.getText().toString());

        if(daysBetween(dateDebut, dateFin) < 0){
            Toast.makeText(this, getString(R.string.error_addrequest_enddatetoosmall), Toast.LENGTH_LONG).show();
            return false;
        }

        RequestEntity newRequest = new RequestEntity();
        newRequest.setIdUser(idUser);

        newRequest.setDateDebut(converters.dateToTimestamp(dateDebut));
        newRequest.setDateFin(converters.dateToTimestamp(dateFin));
        newRequest.setRemark(remarks.getText().toString());
        newRequest.setIdStatus(new Long(1));
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

        return true;

    }

    private void showDatePicketDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + (month+1) + "/" + year;

        if (SELECTED == "STARTDATE")
            startDate.setText(date);
        else if (SELECTED == "ENDDATE")
            endDate.setText(date);
    }

    public int daysBetween(Date d1, Date d2) {
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

}
