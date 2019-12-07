package com.example.workplanner.databases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseQuery extends com.example.workplanner.databases.DatabaseObject {



    public DatabaseQuery(Context context) {
        super(context);
    }

    private static final String TAG = "Databas";

    @SuppressLint("SimpleDateFormat")
    public List<EventObjects> getAllFutureEvents(Date mDate) throws ParseException {
        Calendar calDate = Calendar.getInstance();
        Calendar dDate = Calendar.getInstance();
        calDate.setTime(mDate);

        int calDay = calDate.get(Calendar.DAY_OF_MONTH);
        int calMonth = calDate.get(Calendar.MONTH) + 1;
        int calYear = calDate.get(Calendar.YEAR);

        List<EventObjects> events = new ArrayList<>();
        String query = "select * from events";
        Cursor cursor = this.getDbConnection().rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String message = cursor.getString(cursor.getColumnIndexOrThrow("message"));
                String startDate = cursor.getString(cursor.getColumnIndexOrThrow("reminder"));
                String endDate = cursor.getString(cursor.getColumnIndexOrThrow("end"));
                //convert start date to date object
                Date reminderDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate);
                Date end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endDate);
                assert reminderDate != null;
                dDate.setTime(reminderDate);
                int dDay = dDate.get(Calendar.DAY_OF_MONTH);
                int dMonth = dDate.get(Calendar.MONTH) + 1;
                int dYear = dDate.get(Calendar.YEAR);
                if(calDay == dDay && calMonth == dMonth && calYear == dYear){
                    events.add(new EventObjects(id, message, reminderDate, end));
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        return events;
    }

}