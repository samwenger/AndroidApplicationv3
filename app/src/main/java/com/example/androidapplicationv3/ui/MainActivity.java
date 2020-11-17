package com.example.androidapplicationv3.ui;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.example.androidapplicationv3.R;
import com.example.androidapplicationv3.database.converters.Converters;
import com.example.androidapplicationv3.database.pojo.RequestWithType;
import com.example.androidapplicationv3.viewmodel.RequestListViewModel;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    private CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM YYYY", Locale.getDefault());

    private List<RequestWithType> requests;
    private RequestListViewModel requestListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // initialize view
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);
        setTitle(getString(R.string.title_activity_main));
        navigationView.setCheckedItem(position);

        // Get userId
        SharedPreferences settings = getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        Long idUser = settings.getLong(BaseActivity.PREFS_IDUSER, 0);


        // setting up the action bar to display the month show in the calendar view
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        Calendar calendar = Calendar.getInstance();
        actionBar.setTitle(dateFormatMonth.format(calendar.getTime()));


        // Configuration of the CompactCalendarView
        compactCalendar = findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        compactCalendar.setShouldShowSelectedDay(false);

        // Set listener on the calendar view to update the month in the action bar on scroll
        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date date) {
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionBar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });


        // Get data from the database
        requests = new ArrayList<>();

        RequestListViewModel.Factory factory = new RequestListViewModel.Factory(getApplication(), idUser);
        requestListViewModel = ViewModelProviders.of(this,factory).get(RequestListViewModel.class);
        requestListViewModel.getRequestsWithInfosByUser().observe(this, requestEntities -> {
            if(requestEntities != null) {
                requests = requestEntities;

                // Create a calendar event for each request found in the database
                for(RequestWithType request : requests){

                    // Get the status of the request and choose the corresponding color
                    Long idStatut = request.request.getIdStatus();

                    if(idStatut != 3){
                        int color = 0;
                        if(idStatut == 1){
                            color = Color.GRAY;
                        }
                        else if (idStatut == 2){
                            Long idType = request.request.getIdType();
                            if(idType == 1){
                                color = hex2Rgb(R.color.colorTypeVacation);
                            } else if(idType == 2){
                               color = hex2Rgb(R.color.colorTypeSpecialLeave);
                            } else if(idType == 3){
                                color = hex2Rgb(R.color.colorTypeOvertime);
                            } else {
                                color = hex2Rgb(R.color.colorTypeLeaveWithoutPay);
                            }
                        }

                        Converters converts = new Converters();
                        Date startDate = converts.fromTimestamp(request.request.getDateDebut());
                        Date endDate = converts.fromTimestamp(request.request.getDateFin());
                        int nbDays = daysBetween(startDate, endDate);

                        calendar.setTime(startDate);

                        // Add an event for each day between the startDay and the endDay
                        for(int i=0; i<=nbDays; i++){
                            Event event = new Event(color, calendar.getTimeInMillis(), "Some extra data that I want to store.");
                            compactCalendar.addEvent(event);
                            calendar.add(Calendar.DATE, 1);
                        }

                    }
                }
                // update Calendar View
                compactCalendar.invalidate();
            }
        });



    }


    /**
     * Update navigation drawer menu on resume
     */
    @Override
    protected void onResume() {
        position = R.id.nav_home;
        navigationView.setCheckedItem(position);
        super.onResume();
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
     * Converting HTML color to an RGB Color and creating it
     * @param colorId
     * @return
     */
    public int hex2Rgb(int colorId) {
        String hex = "#" + Integer.toHexString(ContextCompat.getColor(this,  colorId) & 0x00ffffff);
        int[] rgb = new int[3];

        rgb[0] = Integer.valueOf( hex.substring( 1, 3 ), 16 );
        rgb[1] = Integer.valueOf( hex.substring( 3, 5 ), 16 );
        rgb[2] = Integer.valueOf( hex.substring( 5, 7 ), 16 );

        return Color.rgb(rgb[0],rgb[1],rgb[2]);
    }



}