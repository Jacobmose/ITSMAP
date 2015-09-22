package com.example.frederikandersen.h4_group_4;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    WeatherService mService;
    boolean mBound = false;

    private TextView txtCity;
    private TextView txtUpdate;
    private TextView txtWeatherInfo;
    private TextView txtTemperature;
    private TextView txtDescription;
    private ImageView igvWeatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCity = (TextView) findViewById(R.id.city);
        txtUpdate = (TextView) findViewById(R.id.update);
        txtWeatherInfo = (TextView) findViewById(R.id.weatherInfo);
        txtTemperature = (TextView) findViewById(R.id.temperature);
        txtDescription = (TextView) findViewById(R.id.discription);
        igvWeatherIcon = (ImageView) findViewById(R.id.weatherIcon);

        LocalBroadcastManager.getInstance(this).registerReceiver(JSONReceiver, new IntentFilter("JSONIntent"));
        LocalBroadcastManager.getInstance(this).registerReceiver(DrawableReceiver, new IntentFilter("DrawableIntent"));

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

    private BroadcastReceiver JSONReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("MainUI", "Broadcast JSON received");
            String JSON = intent.getExtras().getString("JSONString");
            updateView(JSON);
        }
    };

    private BroadcastReceiver DrawableReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("MainUI", "Broadcast Icon received");
            Bitmap drawable = (Bitmap) intent.getParcelableExtra("Drawable");
            igvWeatherIcon.setImageBitmap(drawable);
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i("ServiceConnection", "OnServiceConnection");
            WeatherService.LocalBinder binder = (WeatherService.LocalBinder) iBinder;
            mService = binder.getService();
            mBound = true;
            mService.startServiceHandler(1000*60*5);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };

    public void updateView(String JSON){
        try{

            Log.i("MainUI", "updateView");

            JSONObject jsonObject = new JSONObject(JSON);

            JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
            JSONObject main = jsonObject.getJSONObject("main");
            JSONObject wind = jsonObject.getJSONObject("wind");
            JSONObject rain = jsonObject.getJSONObject("rain");
            DateFormat df = new SimpleDateFormat("HH.mm.ss '-' dd.MM.yyyy");

            txtCity.setText("Århus");

            txtUpdate.setText("Last Updated: " + df.format(Calendar.getInstance().getTime()));

            txtTemperature.setText(String.format("%.0f", main.getDouble("temp")) + " ℃");

            txtDescription.setText(weather.getString("description").toUpperCase());

            txtWeatherInfo.setText(
                    "Wind Speed: " + wind.getString("speed") + " m/s" + "\n" +
                            "Humidity: " + main.getString("humidity") + " %" + "\n" +
                            "Pressure: " + main.getString("pressure") + " hPa" + "\n" +
                            "Rain: " + rain.getString("3h")
            );



            mService.startIconRequest("http://openweathermap.org/img/w/" + weather.getString("icon") + ".png");


        } catch (JSONException ex){
            Log.i("MainUI", "updateView Exception");
        }

    }
}
