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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jacobmosehansen.themeproject.Chat.MessageService;
import com.jacobmosehansen.themeproject.MainActivity;
import com.jacobmosehansen.themeproject.R;
import com.jacobmosehansen.themeproject.Tools.DBUserAdapter;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin, btnCreateUser;
    EditText edLoginEmail, edLoginPassword;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String email;
    String password;
    Integer userId;
    Intent intent;
    Intent serviceIntent;
    DBUserAdapter dbUser;
    ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        currentUser = ParseUser.getCurrentUser();

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnCreateUser = (Button) findViewById(R.id.btn_createUser);
        edLoginEmail = (EditText) findViewById(R.id.edLoginEmail);
        edLoginPassword = (EditText) findViewById(R.id.edLoginPassword);

        intent = new Intent(LoginActivity.this, MainActivity.class);
        serviceIntent = new Intent(LoginActivity.this, MessageService.class);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = edLoginEmail.getText().toString();
                password = edLoginPassword.getText().toString();

                if (email.length() > 0 && password.length() > 0) {

                        if(currentUser == null)
                        {
                            ParseUser.logInInBackground(email, password, new LogInCallback() {
                                public void done(ParseUser user, com.parse.ParseException e) {
                                    if (user != null) {
                                        Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                        startService(serviceIntent);
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_LONG).show();
                            startActivity(intent);
                            startService(serviceIntent);
                            Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();

                        }
                } else {
                    Toast.makeText(LoginActivity.this, "Email or Password cannot be null", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateUserActivity.class);
                startActivity(intent);
            }
        });


    }

    private void savePreferences(String key, int userId){

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        editor.putInt(key, userId);
        editor.commit();
    }

    private void loadSavedPreferences(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getInt("USER_ID", 0);
    }
}
