package com.jacobmosehansen.themeproject.Tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import com.jacobmosehansen.themeproject.Profile.UserProfile;


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
    public static final String KEY_PARSEID = "parseid";
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
                    + "picture blob, "
                    + "password text not null, "
                    + "parseid text);";

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

    public long AddUser(String username, String age, String gender, String email, String password, String parseId) {

        String ra = "0";
        String r = "0.0";

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_USERNAME, username);
        initialValues.put(KEY_AGE, age);
        initialValues.put(KEY_GENDER, gender);
        initialValues.put(KEY_RATINGAMOUNT, ra);
        initialValues.put(KEY_RATING, r);
        initialValues.put(KEY_EMAIL, email);
        initialValues.put(KEY_PASSWORD, password);
        initialValues.put(KEY_PARSEID, parseId);

        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean Login(String email, String password) throws SQLException {
        Cursor mCursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE email=? AND password=?", new String[]{email, password});
        if (mCursor != null) {
            if(mCursor.getCount() > 0)
            {
                mCursor.close();
                return true;
            }
        }
        return false;
    }

    public UserProfile getUserProfile(int id){
        db = DBHelper.getReadableDatabase();

        UserProfile userProfile;

        Cursor mCursor = db.query(DATABASE_TABLE, new String[]{KEY_USERNAME, KEY_EMAIL, KEY_AGE, KEY_GENDER, KEY_RATINGAMOUNT, KEY_RATING, KEY_PICTURE, KEY_PARSEID},
                KEY_ROWID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (mCursor != null){
            mCursor.moveToFirst();
        }
            userProfile = new UserProfile(
                    mCursor.getString(0),
                    mCursor.getString(1),
                    mCursor.getString(2),
                    mCursor.getString(3),
                    mCursor.getString(4),
                    mCursor.getString(5),
                    mCursor.getBlob(6),// Kan være jeg skal ignorer picture i getUserProfile, men force get den.
                    mCursor.getString(7));

            return userProfile;
    }

    public List<UserProfile> getAllUserProfiles() {
        List<UserProfile> profilesList = new ArrayList<UserProfile>();

        db = DBHelper.getReadableDatabase();

        Cursor mCursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);

        if (mCursor != null) {
            do {
                UserProfile userProfile = new UserProfile();
                userProfile.setName(mCursor.getString(1));
                userProfile.setAge(mCursor.getString(2));
                userProfile.setGender(mCursor.getString(3));
                userProfile.setRatingAmount(mCursor.getString(4));
                userProfile.setRating(mCursor.getString(5));

                profilesList.add(userProfile);

            } while (mCursor.moveToNext());
        }
        return profilesList;
    }

    public int getRating(String id)throws SQLException {
        int _id = 0;
        Cursor mCursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE rating=?", new String[]{id});
        if (mCursor != null) {
            if (mCursor.moveToFirst()){
                _id = mCursor.getInt(mCursor.getColumnIndex("rating"));

                return _id;
            }
        }
        return _id;
    }

    public int getRatingAmount(String id)throws SQLException {
        int _id = 0;
        Cursor mCursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_RATINGAMOUNT + "=?", new String[]{id});
        if (mCursor != null) {
            if (mCursor.moveToFirst()){
                _id = mCursor.getInt(mCursor.getColumnIndex(KEY_RATINGAMOUNT));

                return _id;
            }
        }
        return _id;
    }

    public Integer setRating(String id, String rating)throws SQLException {
        db = DBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_RATING, rating);
        return db.update(DATABASE_TABLE, values, KEY_ROWID + " =?", new String[]{id});
    }

    public Integer setRatingAmount(String id, String rating_amount)throws SQLException {
        db = DBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_RATINGAMOUNT, rating_amount);
        return db.update(DATABASE_TABLE, values, KEY_ROWID + " =?", new String[]{id});
    }


    public int getUserId(String email)throws SQLException {
        int id = 0;
        Log.d("get user id", email);
        Cursor mCursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE email=?", new String[]{email});
        if (mCursor != null) {
            if (mCursor.moveToFirst()){
                Log.d("get user id", "true");
                id = mCursor.getInt(mCursor.getColumnIndex("_id"));

                return id;
            }
        }
        return id;
    }

    //Insert bytearray with blob
    public Integer insertImage(String id, Bitmap image)throws SQLException{
        byte[] data = getBitmapAsByteArray(image);
        db = DBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PICTURE, data);
        return db.update(DATABASE_TABLE, values, KEY_ROWID + " =?", new String[]{id});
    }

    //Convert to bytearray
    public static byte[] getBitmapAsByteArray(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    //Get image from bytearray
    public Bitmap getImage(String id){

        Log.d("Entering", "Entering");
        Cursor mCursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_PICTURE + "=?", new String[]{id});
        byte[] data = mCursor.getBlob(0);
        if (mCursor != null) {
            if (mCursor.moveToFirst()){
                //byte[] data = mCursor.getBlob(0);
                mCursor.close();
                Log.d("Cursor", "jepjep");
                return BitmapFactory.decodeByteArray(data, 0, data.length);
            }
        }
        Log.d("Exiting", "Exiting");
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }
}

