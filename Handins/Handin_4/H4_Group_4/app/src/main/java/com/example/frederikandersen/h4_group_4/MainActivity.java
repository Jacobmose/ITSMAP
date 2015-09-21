package com.example.frederikandersen.h4_group_4;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    WeatherService mService;
    boolean mBound = false;

    private TextView txtview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtview = (TextView) findViewById(R.id.textView);
    }

    @Override
    protected void onStart(){
        super.onStart();

        Intent intent = new Intent(this, WeatherService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        //String mystring = mService.getWeatherJSON();


        //txtview.setText(mystring);
    }

    @Override
    protected void onStop(){
        super.onStop();

        if(mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            WeatherService.LocalBinder binder = (WeatherService.LocalBinder) iBinder;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };
}
