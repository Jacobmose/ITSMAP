package jrt.e15.itsmap.iha.dk.persistenceapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jrt on 14-09-2015.
 * Source and idea from
 * http://www.androiddesignpatterns.com/2012/05/correctly-managing-your-sqlite-database.html
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper sInstance;


    public static synchronized DatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, DAO.DATABASE_NAME, null, DAO.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DAO.TABLE_NAME
                + "(id INTEGER PRIMARY KEY, name TEXT)");
    }


    /**
     * This upgrade is rather brutal.
     * Instead following guide lines given in
     * https://thebhwgroup.com/blog/how-android-sqlite-onupgrade
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v("PersistenceApp DBHelper","onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS " + DAO.TABLE_NAME);
        onCreate(db);
    }
}
