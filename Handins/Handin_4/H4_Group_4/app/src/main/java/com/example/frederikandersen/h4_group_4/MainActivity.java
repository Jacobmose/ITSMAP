package com.example.frederikandersen.h4_group_4;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    WeatherService mService;
    boolean mBound = false;

    private TextView txtview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtview = (TextView) findViewById(R.id.textView);

        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, new IntentFilter("weatherMessage"));

    }

    @Override
    protected void onStart(){
        super.onStart();

        Intent intent = new Intent(this, WeatherService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop(){
        super.onStop();

        if(mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    public void onButtonClick(View v) {
        if (mBound) {

            mService.startWeatherRequest();

        }
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("MainUI", "Broadcast received");
            String mystring = intent.getExtras().getString("JSONString");
            txtview.setText(mystring);
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i("ServiceConnection", "OnServiceConnection");
            WeatherService.LocalBinder binder = (WeatherService.LocalBinder) iBinder;
            mService = binder.getService();
            mBound = true;
            mService.startServiceHandler(10000);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };
}
