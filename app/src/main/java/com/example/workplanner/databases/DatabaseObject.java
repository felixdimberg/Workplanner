package com.example.workplanner.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseObject {
    private static com.example.workplanner.databases.Database dbHelper;
    private SQLiteDatabase db;
    public DatabaseObject(Context context) {
        dbHelper = new com.example.workplanner.databases.Database(context);
        this.dbHelper.getWritableDatabase();
        this.db = dbHelper.getReadableDatabase();
    }
    public SQLiteDatabase getDbConnection(){
        return this.db;
    }
    public void closeDbConnection(){
        if(this.db != null){
            this.db.close();
        }
    }
}