package com.jacobmosehansen.h3_group_4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class MySQLiteHelper extends SQLiteOpenHelper{

    public static final String TABLE_NAME = "notetable";
    public static final String TABLE_NOTETEXT = "NoteText";
    public static final String TABLE_DATETIME = "DateTime";
    private static final String DATABASE_NAME = "comments.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME
            + "(" + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TABLE_NOTETEXT + " TEXT, "
            + TABLE_DATETIME + " TEXT )";


    private static MySQLiteHelper sInstance;

    public static synchronized MySQLiteHelper getsInstance(Context context) {
        if(sInstance == null){
            sInstance = new MySQLiteHelper((context.getApplicationContext()));
        }
        return sInstance;
    }

    public MySQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database){
        Log.i("MySQLiteHelper", "onCreate");
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("MySQLiteHelper", "onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
