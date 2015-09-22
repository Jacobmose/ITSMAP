package com.example.frederikandersen.h4_group_4;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class WeatherService extends Service {

    private String HTTP_ADDRESS = "http://api.openweathermap.org/data/2.5/weather?id=2624652&units=metric";


    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder{
        WeatherService getService(){
            Log.i("WeatherService", "getService");
            return WeatherService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void startServiceHandler(long timeInMilliSec){
        final android.os.Handler handler = new android.os.Handler();

        Timer timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        startWeatherRequest();
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, timeInMilliSec);
    }

    private class DownloadweatherData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls){
            Log.i("weatherService", "doInBackground");
            return getWeatherJSON(urls[0]);
        }
        @Override
        protected void onPostExecute(String result){
            Log.i("weatherService", "onPostExecute");
            Intent broadcastIntent = new Intent("weatherMessage");
            broadcastIntent.putExtra("JSONString", result);
            LocalBroadcastManager.getInstance(WeatherService.this).sendBroadcast(broadcastIntent);
        }
    }

    public void startWeatherRequest(){
        Log.i("weatherService", "startWeatherRequest");
        new DownloadweatherData().execute(HTTP_ADDRESS);

    }

    public String getWeatherJSON(String myURL) {

       HttpURLConnection connection = null;

       try {
           URL url = new URL(myURL);
           connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("GET");
           connection.connect();

           BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
           StringBuilder stringBuilder = new StringBuilder();

           String line;
           while((line = bufferedReader.readLine()) != null){
               stringBuilder.append((line+"\n"));
           }
           bufferedReader.close();
           return stringBuilder.toString();
       }
       catch (MalformedURLException ex) {
           Log.i("getWeatherJSON", "MalformedURLException");
           return "";
       }
       catch (IOException ex){
           Log.i("getWeatherJSON", "IOException");
           return "";
       }
   }

}
