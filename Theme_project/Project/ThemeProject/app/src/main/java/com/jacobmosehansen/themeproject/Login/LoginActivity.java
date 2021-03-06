package com.jacobmosehansen.themeproject.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.jacobmosehansen.themeproject.Chat.MessageService;
import com.jacobmosehansen.themeproject.MainActivity;
import com.jacobmosehansen.themeproject.R;
import com.jacobmosehansen.themeproject.Tools.ParseAdapter;
import com.parse.LogInCallback;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 **  The location used in this activity is greatly inspired from the following link:
 **  http://www.androidhive.info/2015/02/android-location-api-using-google-play-services/
 **/

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private GoogleApiClient mGoogleApiClient;

    private Location mLastLocation;
    private Geocoder geocoder;
    private String _actualLocation;

    private double _latitude;
    private double _longitude;

    Button btnLogin, btnCreateUser;
    EditText edLoginEmail, edLoginPassword;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String email;
    String password;
    Integer userId;
    Intent intent;
    Intent serviceIntent;
    private ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (checkPlayServices()) {
            buildGoogleApiClient();
        }

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
                            ParseUser.logInInBackground(email, password, new LogInCallback() {
                                public void done(ParseUser user, com.parse.ParseException e) {

                                    if (user != null) {
                                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.toast_login_success), Toast.LENGTH_SHORT).show();
                                        currentUser = ParseUser.getCurrentUser();
                                        getLocation();
                                        startActivity(intent);
                                        startService(serviceIntent);
                                    } else {
                                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.toast_invalid_email_password), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.toast_null_email_password), Toast.LENGTH_LONG).show();
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

    private void getLocation(){

        Log.d(TAG, "Entered getLocation()");

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        geocoder = new Geocoder(this, Locale.getDefault());

        if (mLastLocation != null){

            _latitude = mLastLocation.getLatitude();
            _longitude = mLastLocation.getLongitude();

            try {
                List<Address> addresses = geocoder.getFromLocation(_latitude, _longitude, 1);
                StringBuilder sb = new StringBuilder();

                Log.d(TAG, "Location is != null");

                if (addresses.size() > 0){
                    Address address = addresses.get(0);
                    sb.append(address.getSubLocality());

                    Log.d(TAG, "Address is found, should be all good");

                    _actualLocation = sb.toString();

                    currentUser.put(ParseAdapter.KEY_LOCATION, _actualLocation);
                    currentUser.saveInBackground();
                }
            } catch (IOException e) {
                Log.d(TAG, "IOException is catched. Location was not found");
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.toast_io_exception), Toast.LENGTH_LONG).show();
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_device_not_supported), Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();

        if(currentUser != null) {
            getLocation();
            startActivity(intent);
            startService(serviceIntent);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroy(){
        stopService(new Intent(this, MessageService.class));
        if(currentUser != null){
            currentUser.logOutInBackground();
        }
        if (mGoogleApiClient != null){
           mGoogleApiClient.disconnect();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
