package com.jacobmosehansen.themeproject.Post;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.jacobmosehansen.themeproject.R;

/**
 * Created by Marlene on 08-10-2015.
 */
    //Skal starte når der trykkes på "Posts" fra starten af.
    //Skal hente data omkring hvilke posts der er i DB'en.
    //Skal initiere fragment "details" samt den nye aktivitet "New post".

public class PostsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        PostListFragment postListFragment = new PostListFragment();
        //PostDetailsFragment postDetailsFragment = new PostDetailsFragment();

        fragmentTransaction.replace(android.R.id.content, postListFragment);
        fragmentTransaction.commit();
    }




    /*private void loadImageFromDB() {
        ParseFile profilePicture = (ParseFile)userProfile.get(ParseAdapter.KEY_PICTURE);
        profilePicture.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                if (e == null){
                    Log.d("Debug", "Picture received");
                    Bitmap picture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    roundImage = new RoundImage(picture);
                    imageView.setImageDrawable(roundImage);
                } else {
                    Log.d("Debug", "Something went wrong");
                }
            }
        });
    }*/
}
