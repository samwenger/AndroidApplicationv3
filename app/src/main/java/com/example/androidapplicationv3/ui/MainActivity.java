package com.example.androidapplicationv3.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.androidapplicationv3.R;
import com.example.androidapplicationv3.database.converters.Converters;
import com.example.androidapplicationv3.database.entity.RequestEntity;
import com.example.androidapplicationv3.database.pojo.RequestWithType;
import com.example.androidapplicationv3.viewmodel.RequestListViewModel;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;

public class MainActivity extends BaseActivity {

    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM YYYY", Locale.getDefault());

    private List<RequestWithType> requests;
    private RequestListViewModel requestListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);

        setTitle(getString(R.string.title_activity_main));
        navigationView.setCheckedItem(position);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        Calendar calendar = Calendar.getInstance();
        actionBar.setTitle(dateFormatMonth.format(calendar.getTime()));


        // Get userId
        SharedPreferences settings = getSharedPreferences(BaseActivity.PREFS_NAME, 0);
        Long idUser = settings.getLong(BaseActivity.PREFS_IDUSER, 0);

        requests = new ArrayList<>();


        compactCalendar = findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        compactCalendar.setShouldShowSelectedDay(false);


        // Get data from the database
        RequestListViewModel.Factory factory = new RequestListViewModel.Factory(getApplication(), idUser);
        requestListViewModel = ViewModelProviders.of(this,factory).get(RequestListViewModel.class);
        requestListViewModel.getRequestsWithInfosByUser().observe(this, requestEntities -> {
            if(requestEntities != null) {
                requests = requestEntities;

                for(RequestWithType request : requests){

                    Long idStatut = request.request.getIdStatus();

                    if(idStatut != 3){
                        int color = 0;

                        if(idStatut == 1){
                            color = Color.GRAY;
                        }
                        else if (idStatut == 2){
                            Long idType = request.request.getIdType();
                            if(idType == 1){
                                color = Color.GREEN;
                            } else if(idType == 2){
                                color = Color.YELLOW;
                            } else if(idType == 3){
                                color = Color.MAGENTA;
                            } else {
                                color = Color.RED;
                            }
                        }

                        Converters converts = new Converters();
                        Date startDate = converts.fromTimestamp(request.request.getDateDebut());
                        Date endDate = converts.fromTimestamp(request.request.getDateFin());
                        int nbDays = daysBetween(startDate, endDate);

                        calendar.setTime(startDate);

                        for(int i=0; i<=nbDays; i++){
                            Event event = new Event(color, calendar.getTimeInMillis(), "Some extra data that I want to store.");
                            compactCalendar.addEvent(event);
                            calendar.add(Calendar.DATE, 1);
                        }

                    }
                }
            }
        });





        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date date) {
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionBar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });

    }


    public int daysBetween(Date d1, Date d2) {
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }



}