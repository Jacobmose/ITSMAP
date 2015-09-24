package com.jacobmosehansen.themeproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;


public class ProfileActivity extends AppCompatActivity {

    RoundImage roundImage;

    ImageView ivProfilePicture;
    RatingBar rbGradRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // set round profile picture //
        ivProfilePicture = (ImageView) findViewById(R.id.imageView_profilePicture);
        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.profile);
        roundImage = new RoundImage(bm);
        ivProfilePicture.setImageDrawable(roundImage);

        // Set ratingbar //
        rbGradRating = (RatingBar) findViewById(R.id.ratingBar_profileRating);
        rbGradRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(getApplicationContext(), Float.toString(rating), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
