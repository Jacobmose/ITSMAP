package com.example.frederikandersen.h4_group_4;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.MalformedInputException;

public class WeatherService extends Service {

    private String HTTP_ADDRESS = "http://api.openweathermap.org/data/2.5/weather?id=2624652&units=metric";

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder{
        WeatherService getService(){
            return WeatherService.this;
        }
    }

   public String getWeatherJSON() {

       HttpURLConnection connection = null;

       try {
           URL url = new URL(HTTP_ADDRESS);
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

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
