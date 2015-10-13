package dk.iha.itsmap.e15.grp03.studybuddy;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Frederik Andersen on 08-10-2015.
 */
public class ThemeProject extends Application {

    public static final String APPID = "WFCQHkQbv0fHPyscTXDxXP5f0u34uDXwOQpwrSo5";
    public static final String CLIENTKEY = "3E5nVQ4lPYZYeqwBaLpmHPh7XHOOz83ZX9t9jDCY";

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, APPID, CLIENTKEY);
    }
}
