package com.example.workplanner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workplanner.databases.DatabaseQuery;
import com.example.workplanner.databases.EventObjects;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView currentDate;
    private TextView currentDay;
    private Calendar cal = Calendar.getInstance();
    private DatabaseQuery mQuery;
    private RelativeLayout mLayout;
    private int eventIndex;
    private boolean indexStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQuery = new DatabaseQuery(this);
        mLayout = findViewById(R.id.left_event_column);
        eventIndex = mLayout.getChildCount();
        currentDate = findViewById(R.id.display_current_date);
        currentDay =findViewById(R.id.display_current_day);
        currentDate.setText(displayDateInString(cal.getTime()));
        currentDay.setText(displayDayInString(cal.getTime()));
        indexStatus = true;
        displayDailyEvents();
        ImageView previousDay = findViewById(R.id.previous_day);
        ImageView nextDay = findViewById(R.id.next_day);
        previousDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousCalendarDate();
            }
        });
        nextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextCalendarDate();
            }
        });
        createEventView(900, 1000, "plugg");
    }
    private void previousCalendarDate(){
        if (indexStatus) {
            mLayout.removeViewAt(eventIndex - 1);
            indexStatus = false;
        }
        Log.d(TAG, "removing index:" + eventIndex);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        currentDate.setText(displayDateInString(cal.getTime()));
        currentDay.setText(displayDayInString(cal.getTime()));
        displayDailyEvents();
    }
    private void nextCalendarDate(){
        if (indexStatus) {
            mLayout.removeViewAt(eventIndex - 1);
            indexStatus = false;
        }        Log.d(TAG, "removing index:" + eventIndex);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        currentDate.setText(displayDateInString(cal.getTime()));
        currentDay.setText(displayDayInString(cal.getTime()));
        displayDailyEvents();
    }
    private String displayDateInString(Date mDate){
        SimpleDateFormat formatter = new SimpleDateFormat("d MMMM, yyyy", Locale.ENGLISH);
        return formatter.format(mDate);
    }
    private String displayDayInString(Date mDate){
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        return formatter.format(mDate);
    }
    private void displayDailyEvents(){
        Date calendarDate = cal.getTime();
        List<EventObjects> dailyEvent = mQuery.getAllFutureEvents(calendarDate);
        for(EventObjects eObject : dailyEvent){
            Date eventDate = eObject.getDate();
            Date endDate = eObject.getEnd();
            String eventMessage = eObject.getMessage();
            int eventBlockHeight = getEventTimeFrame(eventDate, endDate);
            Log.d(TAG, "Height " + eventBlockHeight);
            displayEventSection(eventDate, eventBlockHeight, eventMessage);
        }
    }
    private int getEventTimeFrame(Date start, Date end){
        long timeDifference = end.getTime() - start.getTime();
        Calendar mCal = Calendar.getInstance();
        mCal.setTimeInMillis(timeDifference);
        int hours = mCal.get(Calendar.HOUR);
        int minutes = mCal.get(Calendar.MINUTE);
        return (hours * 60) + ((minutes * 60) / 100);
    }
    private void displayEventSection(Date eventDate, int height, String message){
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        String displayValue = timeFormatter.format(eventDate);
        String[]hourMinutes = displayValue.split(":");
        int hours = Integer.parseInt(hourMinutes[0]);
        int minutes = Integer.parseInt(hourMinutes[1]);
        Log.d(TAG, "Hour value " + hours);
        Log.d(TAG, "Minutes value " + minutes);
        int topViewMargin = (hours * 60) + ((minutes * 60) / 100);
        Log.d(TAG, "Margin top " + topViewMargin);
        createEventView(topViewMargin, height, message);
    }
    private void createEventView(int topMargin, int height, String message){
        TextView mEventView = new TextView(MainActivity.this);
        RelativeLayout.LayoutParams lParam = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lParam.topMargin = topMargin * 2;
        lParam.leftMargin = 24;
        mEventView.setLayoutParams(lParam);
        mEventView.setPadding(24, 0, 24, 0);
        mEventView.setHeight(height * 2);
        mEventView.setGravity(0x11);
        mEventView.setTextColor(Color.parseColor("#ffffff"));
        mEventView.setText(message);
        mEventView.setBackgroundColor(Color.parseColor("#3F51B5"));
        mLayout.addView(mEventView, eventIndex - 1);
        Log.d(TAG, "adding index:" + eventIndex);
        indexStatus = true;
    }

    public void launchAddActivity(View view) {
        Intent intent = new Intent (this, AddActivity.class);
        startActivity(intent);
    }

    public void launchSettingActivity(View view) {
    }
}