package com.jacobmosehansen.h2_group_4;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class TimerService extends Service {

    private Intent activity2Intent;
    private Intent broadcastIntent;

    public TimerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate(){


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        Log.i("TimerService", "onStartCommand called");
        String data = (String) intent.getExtras().get("Message");
        int time = (Integer) intent.getExtras().get("Time");

        activity2Intent = new Intent(TimerService.this, Activity2.class);
        activity2Intent.putExtra("Message", data);
        broadcastIntent = new Intent("timeMessage");

        activity2Intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);



        new CountDownTimer(time * 1000, 1000) {

            @Override
            public void onTick(long milliUntilFinished) {
                broadcastIntent.putExtra("ProgressTime", milliUntilFinished / 1000);
                LocalBroadcastManager.getInstance(TimerService.this).sendBroadcast(broadcastIntent);
                Log.i("TimerService", "onTick called");
            }

            @Override
            public void onFinish() {
                broadcastIntent.putExtra("ProgressTime", 0);
                LocalBroadcastManager.getInstance(TimerService.this).sendBroadcast(broadcastIntent);
                startActivity(activity2Intent);
                Log.i("TimerService", "onFinish called");
            }
        }.start();


        return START_STICKY;
    }
}
