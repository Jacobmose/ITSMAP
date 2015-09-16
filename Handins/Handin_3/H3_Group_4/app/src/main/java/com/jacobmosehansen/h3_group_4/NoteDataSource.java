package com.jacobmosehansen.h3_group_4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NoteDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper sqlHelper;

    public NoteDataSource(Context context) {
        sqlHelper = MySQLiteHelper.getsInstance(context);
        this.database = sqlHelper.getWritableDatabase();
    }

    public void close() {
        sqlHelper.close();
    }

    public void deleteAll() {
		this.database.delete(MySQLiteHelper.TABLE_NAME, null, null);
    }


    public void insertNote(String note){

        DateFormat df = new SimpleDateFormat("HH.mm.ss '-' dd.MM.yyyy");
        String datetime = df.format(Calendar.getInstance().getTime());

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.TABLE_NOTETEXT, note);
        values.put(MySQLiteHelper.TABLE_DATETIME, datetime);

        database.insert(MySQLiteHelper.TABLE_NAME, null, values);
    }

    public List<String> getAllNotes()  {
        List<String> list = new ArrayList<>();
        Cursor cursor = this.database.query(MySQLiteHelper.TABLE_NAME, new String[]{MySQLiteHelper.TABLE_NOTETEXT, MySQLiteHelper.TABLE_DATETIME}, null, null, null, null, null);

        if(cursor.moveToFirst()){
            do {
                list.add(cursor.getString(0) + " " + cursor.getString(1));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        return list;

    }
}


































