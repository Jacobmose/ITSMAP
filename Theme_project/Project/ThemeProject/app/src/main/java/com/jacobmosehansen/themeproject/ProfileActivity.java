package com.jacobmosehansen.themeproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;

public class ProfileActivity extends AppCompatActivity {

    RoundImage roundImage;

    ImageView ivProfilePicture;
    RatingBar rbGradRating;
    Spinner sprSubjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Set Round Profile Picture //
        ivProfilePicture = (ImageView) findViewById(R.id.imageView_profilePicture);
        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.default_profile);
        roundImage = new RoundImage(bm);
        ivProfilePicture.setImageDrawable(roundImage);

        // Set Rating Bar //
        rbGradRating = (RatingBar) findViewById(R.id.ratingBar_profileRating);

        // Set Subject Spinner //
        sprSubjects = (Spinner) findViewById(R.id.spinner_subjects);
        ArrayAdapter<CharSequence> myAdapter = ArrayAdapter.createFromResource(this,
                R.array.subjects_array, android.R.layout.simple_spinner_item);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sprSubjects.setPrompt("Select a subject");

        // Set Subject Selection Preset Text //
        sprSubjects.setAdapter(new NothingSelectedSpinnerAdapter(myAdapter,
                R.layout.spinner_row_nothing_selected,this));


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
