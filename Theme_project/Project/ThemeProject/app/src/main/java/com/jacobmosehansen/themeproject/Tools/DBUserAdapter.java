package com.jacobmosehansen.themeproject.Tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Jacobmosehansen on 03-10-2015.
 */

public class DBUserAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_USERNAME= "username";
    public static final String KEY_EMAIL= "email";
    public static final String KEY_AGE= "age";
    public static final String KEY_GENDER= "gender";
    public static final String KEY_RATINGAMOUNT= "rating_amount";
    public static final String KEY_RATING= "rating";
    public static final String KEY_SUBJECTS= "subjects";
    public static final String KEY_PICTURE= "picture";
    public static final String KEY_PASSWORD = "password";
    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "usersdb";
    private static final String DATABASE_TABLE = "users";
    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_CREATE =
            "create table users (_id integer primary key autoincrement, "
                    + "username text not null, "
                    + "email text not null, "
                    + "age text not null, "
                    + "gender text not null, "
                    + "rating_amount text, "
                    + "rating text, "
                    + "subjects text, "
                    + "picture text, "
                    + "password text not null);";

    private Context context = null;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBUserAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion
                    + " to "
                    + newVersion);
            db.execSQL("DROP TABLE IF EXISTS users");
            onCreate(db);
        }
    }


    public void open() throws SQLException {
        db = DBHelper.getWritableDatabase();
    }


    public void close() {
        DBHelper.close();
    }

    public long AddUser(String username, String age, String gender, String email, String password) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_USERNAME, username);
        initialValues.put(KEY_AGE, age);
        initialValues.put(KEY_GENDER, gender);
        initialValues.put(KEY_EMAIL, email);
        initialValues.put(KEY_PASSWORD, password);

        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean Login(String email, String password) throws SQLException {
        Cursor mCursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE email=? AND password=?", new String[]{email,password});
        if (mCursor != null) {
            if(mCursor.getCount() > 0)
            {
                mCursor.close();
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getUser(int id){
        Cursor mCursor = db.query(DATABASE_TABLE, new String[]{ KEY_ROWID, KEY_USERNAME, KEY_AGE, KEY_GENDER }, KEY_ROWID + "=?", new String[] {String.valueOf(id)}, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        int userId = Integer.parseInt(mCursor.getString(0));
        String userName = mCursor.getString(1);
        String userAge = mCursor.getString(2);
        String userGender = mCursor.getString(4);

        ArrayList<String> data = new ArrayList<String>();
        data.add(Integer.toString(userId));
        data.add(userName);
        data.add(userAge);
        data.add(userGender);

        return data;
    }

    public int getUserId(String email)throws SQLException {
        int id = 0;
        Cursor mCursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE email=?", new String[]{email});
        if (mCursor != null) {
            if (mCursor.moveToFirst()){
                id = mCursor.getInt(mCursor.getColumnIndex("_id"));

                return id;
            }
        }
        return id;
    }

}

