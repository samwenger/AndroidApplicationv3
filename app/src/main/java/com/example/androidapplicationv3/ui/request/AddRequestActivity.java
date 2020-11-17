package com.example.androidapplicationv3.ui.request;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    private Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // initialize view
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

        Button button = findViewById(R.id.buttonSaveRequest);


        // Get the current user ID
        SharedPreferences settings = getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        idUser = settings.getLong(BaseActivity.PREFS_IDUSER, 0);


        // Set listener on start and date to open a datePickerDialog
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SELECTED = "STARTDATE";
                showDatePickerDialog();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SELECTED = "ENDDATE";
                showDatePickerDialog();
            }
        });


        // Set listener on delete button
        button.setOnClickListener(view -> {
            try {
                if(addRequest()){
                   onBackPressed();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * Saving new request into the database
     * @return
     * @throws ParseException
     */
    private boolean addRequest() throws ParseException {

        int selectedId = radioButtonGroup.getCheckedRadioButtonId();
        Long typeId = null;

        // Get the selected type of leave and defining the ID to store in the database
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


        // Checking if the dates matches the pattern before trying to save in the database

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

        // Converting from string to date to check if the end date is after the start date
        Date dateDebut = new SimpleDateFormat("dd/MM/yyyy").parse(startDate.getText().toString());
        Date dateFin = new SimpleDateFormat("dd/MM/yyyy").parse(endDate.getText().toString());

        if(daysBetween(dateDebut, dateFin) < 0){
            Toast.makeText(this, getString(R.string.error_addrequest_enddatetoosmall), Toast.LENGTH_LONG).show();
            return false;
        }


        // Creating a new request Entity and setting attributes with values
        RequestEntity newRequest = new RequestEntity();
        newRequest.setIdUser(idUser);

        newRequest.setDateDebut(converters.dateToTimestamp(dateDebut));
        newRequest.setDateFin(converters.dateToTimestamp(dateFin));
        newRequest.setRemark(remarks.getText().toString());
        newRequest.setIdStatus(new Long(1));
        newRequest.setIdType(typeId);


        // Saving into database
        new AddRequest(getApplication(), new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                setResponse(true);
            }

            @Override
            public void onFailure(Exception e) {
                setResponse(false);
            }
        }).execute(newRequest);

        return true;
    }


    /**
     * Method to show the DatePickerDialog
     */
    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    /**
     * Take the value choosed in the DatePickerDialog and saving it into startDate or endDate
     * @param view
     * @param year
     * @param month
     * @param dayOfMonth
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + (month+1) + "/" + year;

        if (SELECTED == "STARTDATE")
            startDate.setText(date);
        else if (SELECTED == "ENDDATE")
            endDate.setText(date);
    }


    /**
     * Calculate number of days between two dates
     * @param d1
     * @param d2
     * @return
     */
    public int daysBetween(Date d1, Date d2) {
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }


    /**
     * Message to user to confirm the result of the action
     * @param response
     */
    private void setResponse(Boolean response) {
        if (response) {
            toast = Toast.makeText(this, getString(R.string.request_submitted_msg), Toast.LENGTH_LONG);
            toast.show();
        } else {
            toast = Toast.makeText(this, getString(R.string.error_request_notedited), Toast.LENGTH_LONG);
        }
    }

}
