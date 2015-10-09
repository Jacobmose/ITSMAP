package com.jacobmosehansen.themeproject.Profile;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ProfileActivity extends AppCompatActivity {

    //DB variables//
    SharedPreferences mySharedPreferences;
    Integer userId;
    Integer requestId;

    //UI variables//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        OwnProfileFragment ownProfileFragment = new OwnProfileFragment();
        AnotherProfileFragment anotherProfileFragment = new AnotherProfileFragment();

        loadSavedPreferences();

        Intent intent = getIntent();
        requestId = intent.getIntExtra("USER_ID", 0);
        Bundle idBundle = new Bundle();
        idBundle.putInt("ID_KEY", requestId);
        anotherProfileFragment.setArguments(idBundle);

        if (requestId.intValue() == userId.intValue()){
            fragmentTransaction.replace(android.R.id.content, ownProfileFragment);
        } else if (requestId.intValue() != userId.intValue()){
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
        userId = mySharedPreferences.getInt("USER_ID", 0);
    }

}
