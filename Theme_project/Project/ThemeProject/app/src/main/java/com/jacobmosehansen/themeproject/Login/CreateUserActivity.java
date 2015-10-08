package com.jacobmosehansen.themeproject.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.jacobmosehansen.themeproject.MainActivity;
import com.jacobmosehansen.themeproject.R;
import com.jacobmosehansen.themeproject.Tools.DBUserAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.Period;

public class CreateUserActivity extends AppCompatActivity {

    private Button btnRegister;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private Spinner sprDay, sprMonth, sprYear;
    private Spinner sprGender;
    private ArrayAdapter<String> dayArrayAdapter;
    private ArrayAdapter<String> monthArrayAdapter;
    private ArrayAdapter<String> yearArrayAdapter;
    private ArrayAdapter<String> genderArrayAdapter;
    private ArrayList<String> dayValues;
    private ArrayList<String> yearValues;
    private ArrayList<String> genderValues;
    private int yourAge;
    Calendar calenderDoB;
    Calendar calenderToday;
    DateTime dobDate;
    DateTime currentDate;
    Period period;
    Integer userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        sprDay = (Spinner) findViewById(R.id.sprDay);
        sprMonth = (Spinner) findViewById(R.id.sprMonth);
        sprYear = (Spinner) findViewById(R.id.sprYear);
        sprGender = (Spinner) findViewById(R.id.spinGender);

        dayValues = new ArrayList<String>();
        for (Integer i = 1; i <= 31; i++){
            dayValues.add(Integer.toString(i));
        }
        dayArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dayValues);
        sprDay.setAdapter(dayArrayAdapter);

        monthArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sa_months));
        sprMonth.setAdapter(monthArrayAdapter);

        yearValues = new ArrayList<String>();
        for (Integer i = 1900; i <= 2015; i++){
            yearValues.add(Integer.toString(i));
        }
        yearArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearValues);
        sprYear.setAdapter(yearArrayAdapter);



        genderValues = new ArrayList<String>();
        genderValues.add("Male");
        genderValues.add("Female");
        genderArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genderValues);
        sprGender.setAdapter(genderArrayAdapter);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputFullName.getText().toString();
                String gender = sprGender.getSelectedItem().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                String day = sprDay.getSelectedItem().toString();
                int month = sprMonth.getCount();
                String year = sprYear.getSelectedItem().toString();

                dobDate = new DateTime(Integer.parseInt(year), month, Integer.parseInt(day), 0, 0, 0, 0);
                currentDate = new DateTime();
                period = new Period(dobDate, currentDate);

                String actualAge = Integer.toString(period.getYears()+1);

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {

                    DBUserAdapter dbUser = new DBUserAdapter(CreateUserActivity.this);

                    dbUser.open();
                    dbUser.AddUser(name, actualAge, gender, email, password);
                    dbUser.close();

                    Intent intent = new Intent(CreateUserActivity.this, LoginActivity.class);

                    Toast.makeText(getApplicationContext(), "User was created!", Toast.LENGTH_SHORT).show();

                    startActivity(intent);

                } else
                    Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void loadSavedPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getInt("USER_ID", 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_user, menu);
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
}
