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
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    WeatherService mService;
    boolean mBound = false;

    private TextView txtCity;
    private TextView txtUpdate;
    private TextView txtTemperature;
    private TextView txtDescription;
    private TextView txtRain;
    private TextView txtWind;
    private TextView txtHumidity;
    private TextView txtPressure;
    private ImageView igvWeatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCity = (TextView) findViewById(R.id.city);
        txtUpdate = (TextView) findViewById(R.id.update);
        txtTemperature = (TextView) findViewById(R.id.temperature);
        txtDescription = (TextView) findViewById(R.id.discription);
        txtRain = (TextView) findViewById(R.id.rain);
        txtWind = (TextView) findViewById(R.id.wind);
        txtHumidity = (TextView) findViewById(R.id.humidity);
        txtPressure = (TextView) findViewById(R.id.pressure);
        igvWeatherIcon = (ImageView) findViewById(R.id.weatherIcon);

    }

    @Override
    protected void onStart(){
        super.onStart();

        Intent intent = new Intent(this, WeatherService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(JSONReceiver, new IntentFilter("JSONIntent"));
        LocalBroadcastManager.getInstance(this).registerReceiver(DrawableReceiver, new IntentFilter("DrawableIntent"));
    }

    @Override
    protected  void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(JSONReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(DrawableReceiver);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

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

            DateFormat df = DateFormat.getDateTimeInstance();

            txtCity.setText("Århus");

            txtUpdate.setText("Last Updated: " + df.format(Calendar.getInstance().getTime()));

            txtTemperature.setText(String.format("%.0f", main.getDouble("temp")) + " ℃");

            txtDescription.setText(weather.getString("description").toUpperCase());

            txtWind.setText("Wind Speed: " + wind.getString("speed") + " m/s");

            txtHumidity.setText("Humidity: " + main.getString("humidity") + " %");

            txtPressure.setText("Pressure: " + main.getString("pressure") + " hPa");

            try{
                JSONObject rain = jsonObject.getJSONObject("rain");
                txtRain.setText("Rain: " + rain.getString("3h"));

            }catch(JSONException ex ){
                txtRain.setText("Rain: None");
            }

            mService.startIconRequest("http://openweathermap.org/img/w/" + weather.getString("icon") + ".png");


        } catch (JSONException ex){
            Log.i("MainUI", "updateView JSONException");
        }

    }
}
