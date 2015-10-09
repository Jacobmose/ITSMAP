package com.jacobmosehansen.themeproject.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

import com.jacobmosehansen.themeproject.Chat.MessageService;
import com.jacobmosehansen.themeproject.MainActivity;
import com.jacobmosehansen.themeproject.R;
import com.jacobmosehansen.themeproject.Tools.DBUserAdapter;
import com.parse.Parse;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CreateUserActivity extends AppCompatActivity {

    private Button btnRegister;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private Spinner sprAge;
    private Spinner sprGender;
    private ArrayAdapter<String> ageArrayAdapter;
    private ArrayAdapter<String> genderArrayAdapter;
    private ArrayList<String> ageValues;
    private ArrayList<String> genderValues;
    Integer userId;
    Intent intent;
    Intent serviceIntent;
    DBUserAdapter dbUser;
    String name;
    String age;
    String gender;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        sprAge = (Spinner) findViewById(R.id.spinAge);
        sprGender = (Spinner) findViewById(R.id.spinGender);

        intent = new Intent(CreateUserActivity.this, LoginActivity.class);
        serviceIntent = new Intent(CreateUserActivity.this, MessageService.class);

        ageValues = new ArrayList<String>();
        for (Integer i = 10; i <= 99; i++){
            ageValues.add(Integer.toString(i));
        }
        ageArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ageValues);
        sprAge.setAdapter(ageArrayAdapter);

        genderValues = new ArrayList<String>();
        genderValues.add("Male");
        genderValues.add("Female");
        genderArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genderValues);
        sprGender.setAdapter(genderArrayAdapter);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = inputFullName.getText().toString();
                age = sprAge.getSelectedItem().toString();
                gender = sprGender.getSelectedItem().toString();
                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {

                    dbUser = new DBUserAdapter(CreateUserActivity.this);

                    ParseUser user = new ParseUser();
                    user.setUsername(email);
                    user.setPassword(password);

                    user.signUpInBackground(new SignUpCallback() {
                        public void done(com.parse.ParseException e) {
                            if (e == null) {
                                dbUser.open();
                                dbUser.AddUser(name, age, gender, email, password, ParseUser.getCurrentUser().getObjectId());
                                dbUser.close();
                                Toast.makeText(getApplicationContext(), "User was created!", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "There was an error signing up." + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
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
