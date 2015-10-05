package com.jacobmosehansen.themeproject.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jacobmosehansen.themeproject.MainActivity;
import com.jacobmosehansen.themeproject.R;
import com.jacobmosehansen.themeproject.Tools.DBUserAdapter;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin, btnCreateUser;
    EditText edLoginEmail, edLoginPassword;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Integer userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnCreateUser = (Button) findViewById(R.id.btn_createUser);
        edLoginEmail = (EditText) findViewById(R.id.edLoginEmail);
        edLoginPassword = (EditText) findViewById(R.id.edLoginPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = edLoginEmail.getText().toString();
                String password = edLoginPassword.getText().toString();

                if (email.length() > 0 && password.length() > 0) {
                    try {

                        DBUserAdapter dbUser = new DBUserAdapter(LoginActivity.this);

                        dbUser.open();

                        final String tmpEmail = "admin";
                        final String tmpName= "admin";
                        final String tmpAge= "admin";
                        final String tmpGender= "admin";
                        final String tmpPassword = "admin";
                        dbUser.AddUser(tmpEmail, tmpName,tmpAge, tmpGender, tmpPassword);

                        if (dbUser.Login(email, password)){

                            savePreferences("USER_ID", dbUser.getUserId(email));

                            Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();
                        }
                        dbUser.close();

                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "Exception hit. This was not supposed to happen... Redirecting to MainActivity anyways", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
