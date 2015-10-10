package com.jacobmosehansen.themeproject.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.jacobmosehansen.themeproject.Chat.MessageService;
import com.jacobmosehansen.themeproject.R;
import com.jacobmosehansen.themeproject.Tools.ParseAdapter;
<<<<<<< HEAD
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
=======
>>>>>>> 4785cab72e4f147a731bea5ab84c70b29f77edb3
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.ArrayList;

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
    DateTime dobDate;
    DateTime currentDate;
    Period period;
    Integer userId;
    Intent intent;
    Intent serviceIntent;
    String name;
    String gender;
    String email;
    String password;
    String actualAge;
    ParseAdapter parse;

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

        intent = new Intent(CreateUserActivity.this, LoginActivity.class);
        serviceIntent = new Intent(CreateUserActivity.this, MessageService.class);

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

        parse = new ParseAdapter();

        genderArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sa_gender));
        sprGender.setAdapter(genderArrayAdapter);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = inputFullName.getText().toString();
                gender = sprGender.getSelectedItem().toString();
                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();


                String day = sprDay.getSelectedItem().toString();
                int month = sprMonth.getCount();
                String year = sprYear.getSelectedItem().toString();

                dobDate = new DateTime(Integer.parseInt(year), month, Integer.parseInt(day), 0, 0, 0, 0);
                currentDate = new DateTime();
                period = new Period(dobDate, currentDate);

                actualAge = Integer.toString(period.getYears()+1);

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {

                    if (isEmailValid(email)) {
                        if (isPasswordValid(password)) {

                            ParseUser user = parse.createParseUser(name, actualAge, gender, email, password);

                            user.signUpInBackground(new SignUpCallback() {
                                public void done(com.parse.ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(getApplicationContext(), "User was created!", Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "There was an error signing up " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalid_password), Toast.LENGTH_LONG).show();

                    }else
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.invalid_email), Toast.LENGTH_LONG).show();

<<<<<<< HEAD


                    user.signUpInBackground(new SignUpCallback() {
                        public void done(com.parse.ParseException e) {
                            if (e == null) {
                                Toast.makeText(getApplicationContext(), "User was created!", Toast.LENGTH_SHORT).show();

                                ParseObject rating = parse.createParseRatingObject(ParseUser.getCurrentUser().getObjectId(), 0.0, 0);
                                rating.saveInBackground();

                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "There was an error signing up " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
=======
>>>>>>> 4785cab72e4f147a731bea5ab84c70b29f77edb3



                } else
                    Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_SHORT).show();
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

    public boolean isPasswordValid(CharSequence password){
        int passwordMinLength = 8;

        if (TextUtils.isEmpty(password) || password.length() < passwordMinLength){
            return false;
        }else
            return true;
    }

    public boolean isEmailValid(CharSequence email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
