package com.jacobmosehansen.themeproject.Profile;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.parse.ParseUser;

public class ProfileActivity extends AppCompatActivity {

    //DB variables//
    SharedPreferences mySharedPreferences;
    ParseUser user;
    String userId;
    String requestId;


    //UI variables//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        OwnProfileFragment ownProfileFragment = new OwnProfileFragment();
        AnotherProfileFragment anotherProfileFragment = new AnotherProfileFragment();

        userId = ParseUser.getCurrentUser().getObjectId();

        //loadSavedPreferences();

        Intent intent = getIntent();
        requestId = intent.getStringExtra("USER_ID");

        Bundle idBundle = new Bundle();
        idBundle.putString("ID_KEY", requestId);
        anotherProfileFragment.setArguments(idBundle);

        Log.d("TEST", userId);
        Log.d("TEST", requestId);

        while(userId == "0"){

        }
        if (requestId.equals(userId)){
            fragmentTransaction.replace(android.R.id.content, ownProfileFragment);
        } else{
            fragmentTransaction.replace(android.R.id.content, anotherProfileFragment);
        }fragmentTransaction.commit();


        // _TODO Set Round Profile Picture// MUST BE IMPLEMENTED AS DEFAULT TO DATABASE //
        /*Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.default_profile);
        roundImage = new RoundImage(bm);
        ivProfilePicture.setImageDrawable(roundImage);
        */
    }

    private void loadSavedPreferences(){
        mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = mySharedPreferences.getString("USER_ID", "0");
    }

}
