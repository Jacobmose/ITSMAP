package com.jacobmosehansen.themeproject.Profile;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.parse.ParseUser;

public class ProfileActivity extends AppCompatActivity {

    //DB variables//
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

        Intent intent = getIntent();
        requestId = intent.getStringExtra("USER_ID");

        Bundle idBundle = new Bundle();
        idBundle.putString("ID_KEY", requestId);
        anotherProfileFragment.setArguments(idBundle);

        if (requestId.equals(userId)){
            fragmentTransaction.replace(android.R.id.content, ownProfileFragment);
        } else{
            fragmentTransaction.replace(android.R.id.content, anotherProfileFragment);
        }fragmentTransaction.commit();


    }

}
