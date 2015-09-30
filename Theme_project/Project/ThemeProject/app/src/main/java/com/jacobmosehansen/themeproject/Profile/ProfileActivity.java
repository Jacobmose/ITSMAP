package com.jacobmosehansen.themeproject.Profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.jacobmosehansen.themeproject.R;
import com.jacobmosehansen.themeproject.Tools.NothingSelectedSpinnerAdapter;
import com.jacobmosehansen.themeproject.Tools.RoundImage;
import com.jacobmosehansen.themeproject.Tools.SwipeDismissListViewTouchListener;


public class ProfileActivity extends AppCompatActivity {

    RoundImage roundImage;

    ImageView ivProfilePicture;
    RatingBar rbGradRating;
    Spinner sprSubjects;
    Button btnAddSubject, btnRate;
    ListView lvSubjects;
    ArrayList<String> subjectArray = new ArrayList<String>();

    private ArrayAdapter<String> mySubjectAdapter;
    private static final int CAMERA_REQUEST = 1888;

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
        btnRate = (Button) findViewById(R.id.btn_rate);

        // Set Subject Spinner //
        sprSubjects = (Spinner) findViewById(R.id.spinner_subjects);
        final ArrayAdapter<CharSequence> myAdapter = ArrayAdapter.createFromResource(this,
                R.array.subjects_array, android.R.layout.simple_spinner_item);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sprSubjects.setPrompt("Select a subject");

        // Set Subject Selection Preset Text //
        sprSubjects.setAdapter(new NothingSelectedSpinnerAdapter(myAdapter,
                R.layout.spinner_row_nothing_selected, this));

        // Do on btn_addSubject add chosen subject to listView //
        btnAddSubject = (Button) findViewById(R.id.btn_addSubject);
        lvSubjects = (ListView) findViewById(R.id.lv_subjects);
        mySubjectAdapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_list_item_1, subjectArray);

        btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String selectedSubject = sprSubjects.getSelectedItem().toString();

                    if(subjectArray.size() < 5){
                    if(subjectArray.contains(selectedSubject)){
                        Toast.makeText(getBaseContext(), "Subject already added", Toast.LENGTH_SHORT).show();
                    }else {
                        mySubjectAdapter.add(selectedSubject);
                        lvSubjects.setAdapter(mySubjectAdapter);
                    }}
                else{Toast.makeText(getBaseContext(), "Too many subjects added", Toast.LENGTH_SHORT).show();}
                }catch(Exception e){
                    Toast.makeText(getBaseContext(), "No subject selected", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // Do on subject swipe remove subject from array //

        // This code is based on the Android-SwipeToDismiss example of use of the
        // SwipeDissmissListViewTouchListener class//
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        lvSubjects,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    mySubjectAdapter.remove(mySubjectAdapter.getItem(position));
                                }
                                mySubjectAdapter.notifyDataSetChanged();
                            }
                        });
        lvSubjects.setOnTouchListener(touchListener);


        // On profile picture press, open camera and take picture for imageView //
        ivProfilePicture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent myCameraIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(myCameraIntent, CAMERA_REQUEST);
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
            Bitmap profilePicture = (Bitmap) data.getExtras().get("data");
            roundImage = new RoundImage(profilePicture);
            ivProfilePicture.setImageDrawable(roundImage);
        }
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
