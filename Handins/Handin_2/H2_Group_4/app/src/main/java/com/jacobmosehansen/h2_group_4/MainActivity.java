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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btnStartService;
    private EditText etMessage;
    private EditText etTime;
    private ProgressBar pbProgressBar;
    private Spinner mspin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("MainActivity", "onCreate called");
        btnStartService = (Button) findViewById(R.id.button);
        etMessage = (EditText) findViewById(R.id.et_message);
        pbProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mspin = (Spinner) findViewById(R.id.spinner);

        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, new IntentFilter("timeMessage"));

        ArrayList<String> items = new ArrayList<>();
        for (int i=0; i <= 55; i++)
        {
            items.add(Integer.toString(i+5) + " sec");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, items);
        mspin.setAdapter(adapter);



        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("MainActivity", "Button Clicked");
                Intent timerIntent = new Intent(MainActivity.this, TimerService.class);

                try {
                    timerIntent.putExtra("Time", mspin.getSelectedItemPosition() + 5);
                    timerIntent.putExtra("Message", etMessage.getText().toString());

                    startService(timerIntent);
                    btnStartService.setEnabled(false);
                }
                catch (NumberFormatException nfe) {
                    Log.e("Error", "Error with input values");
                }
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


    private BroadcastReceiver myReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("MainActivity", "Broadcast Reciever");
            Long pbTime = intent.getExtras().getLong("ProgressTime");
            pbProgressBar.setMax(mspin.getSelectedItemPosition()+5);
            pbProgressBar.setProgress(mspin.getSelectedItemPosition()+5-pbTime.intValue());
            if(pbTime == 0)
            {
                btnStartService.setEnabled(true);
            }
        }

    };
}
