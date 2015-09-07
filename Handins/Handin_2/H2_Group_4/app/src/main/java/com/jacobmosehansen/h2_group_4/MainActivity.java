package com.jacobmosehansen.h2_group_4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private Button btnStartService;
    private EditText etMessage;
    private EditText etTime;
    private ProgressBar pbProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartService = (Button) findViewById(R.id.button);
        etMessage = (EditText) findViewById(R.id.et_message);
        etTime = (EditText) findViewById(R.id.et_time);
        pbProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        LocalBroadcastManager.getInstance(this).registerReceiver(myReciever, new IntentFilter("timeMessage"));

        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent timerIntent = new Intent(MainActivity.this, TimerService.class);

                try {
                    timerIntent.putExtra("Time", Integer.parseInt(etTime.getText().toString()));
                    timerIntent.putExtra("Message", etMessage.getText().toString());

                    startService(timerIntent);
                    btnStartService.setEnabled(false);
                }
                catch (NumberFormatException nfe) {}
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private BroadcastReceiver myReciever = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            Long pbTime = intent.getExtras().getLong("ProgressTime");
            pbProgressBar.setMax(Integer.parseInt(etTime.getText().toString()));
            pbProgressBar.setProgress(Integer.parseInt(etTime.getText().toString())-pbTime.intValue());
            if(pbTime == 0)
            {
                btnStartService.setEnabled(true);
            }
        }

    };
}
