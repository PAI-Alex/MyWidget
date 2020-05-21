package com.example.mywidget;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class InfoRepository {
    private static InfoRepository instance;
    DBHelper helper;
    SQLiteDatabase database;
    SimpleDateFormat format;

    private InfoRepository(Context context) {
        helper = new DBHelper(context);
        database = helper.getWritableDatabase();
        format = new SimpleDateFormat("dd/MM/yyyy HH");
    }

    public static InfoRepository createInstance(Context context) {
        if(instance == null) {
            instance = new InfoRepository(context);
        }
        return instance;
    }
    public void insertDate(Date date) {
        database.delete("INFO", null, null);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        date = calendar.getTime();
        ContentValues values = new ContentValues();
        values.put("DATE", format.format(date));
        database.insert("INFO", null, values);
    }
    public Date getDate() {
        Cursor cursor = database.query("INFO", null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            int dateColumn = cursor.getColumnIndex("DATE");
            String result = cursor.getString(dateColumn);
            try {
                return format.parse(result);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        return  null;
    }
    public static InfoRepository getInstance() {
        return instance;
    }
}
