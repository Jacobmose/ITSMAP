package com.jacobmosehansen.themeproject.Profile;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;


// MUST BE DELETED//
/*
import com.jacobmosehansen.themeproject.Tools.NothingSelectedSpinnerAdapter;
import com.jacobmosehansen.themeproject.Tools.RoundImage;
import com.jacobmosehansen.themeproject.Tools.SwipeDismissListViewTouchListener;
*/

public class ProfileActivity extends AppCompatActivity {

    //User/Database variables//
    SharedPreferences mySharedPreferences;
    Integer userId;
    Integer requestId;

    //UI variables//
    TextView mytextview;

    // MUST BE DELETED//
    /*
    RoundImage roundImage;
    ImageView ivProfilePicture;
    RatingBar rbGradRating;
    Spinner sprSubjects;
    Button btnAddSubject, btnRate;
    ListView lvSubjects;
    ArrayList<String> subjectArray = new ArrayList<String>();

    private ArrayAdapter<String> mySubjectAdapter;
    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_FILE = 0;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        OwnProfileFragment ownProfileFragment = new OwnProfileFragment();
        AnotherProfileFragment anotherProfileFragment = new AnotherProfileFragment();

        Intent intent = getIntent();
        requestId = intent.getIntExtra("USER_ID", 0);

        loadSavedPreferences();

        if (requestId.intValue() == userId.intValue()){
            fragmentTransaction.replace(android.R.id.content, ownProfileFragment);
        } else {
            fragmentTransaction.replace(android.R.id.content, anotherProfileFragment);
        }fragmentTransaction.commit();


        // MUST BE DELETED//
        /*
        setContentView(R.layout.activity_profile);
        mySubjectAdapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_list_item_1, subjectArray);
        ivProfilePicture = (ImageView) findViewById(R.id.imageView_profilePicture);
        rbGradRating = (RatingBar) findViewById(R.id.ratingBar_profileRating);
        btnRate = (Button) findViewById(R.id.btn_rate);
        sprSubjects = (Spinner) findViewById(R.id.spinner_subjects);
        btnAddSubject = (Button) findViewById(R.id.btn_addSubject);
        lvSubjects = (ListView) findViewById(R.id.lv_subjects);

        // Set Round Profile Picture //
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.default_profile);
        roundImage = new RoundImage(bm);
        ivProfilePicture.setImageDrawable(roundImage);



        // On profile picture press, open camera and take picture for imageView //
        ivProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        // Set Subject Spinner //
        final ArrayAdapter<CharSequence> myAdapter = ArrayAdapter.createFromResource(this,
                R.array.subjects_array, android.R.layout.simple_spinner_item);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sprSubjects.setPrompt("Select a subject");

        // Set Subject Selection Preset Text //
        sprSubjects.setAdapter(new NothingSelectedSpinnerAdapter(myAdapter,
                R.layout.spinner_row_nothing_selected, this));

        // Do on btn_addSubject add chosen subject to listView //
        btnAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFromSpinner();
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
        */
    }

    private void loadSavedPreferences(){
        mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = mySharedPreferences.getInt("USER_ID", 0);
    }


    // MUST BE DELETED _ CPY //
    /*
    // Note by Morten: //
    // This function was inspired by the tutorial http://www.theappguruz.com/blog/android-take-photo-camera-gallery-code-sample//
    // Function makes it posible for user to either take new picture or select a picture from the library for profile picture//
    private void selectImage(){
        final CharSequence[] options = {"Take new photo", "Choose photo from library", "Cancel"};

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProfileActivity.this);
        dialogBuilder.setTitle("Add profile picture");
        dialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take new photo")) {
                    Intent myPhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(myPhotoIntent, CAMERA_REQUEST);
                } else if (options[item].equals("Choose photo from library")) {
                    Intent myPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    myPhotoIntent.setType("image/*");
                    startActivityForResult(Intent.createChooser(myPhotoIntent, "Select File"), SELECT_FILE);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        dialogBuilder.show();
    }


    // Note by Morten: //
    // This function was inspired by the tutorial http://www.theappguruz.com/blog/android-take-photo-camera-gallery-code-sample//
    // Function makes it posible for user to either take new picture or select a picture from the library for profile picture//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == CAMERA_REQUEST) {
                Bitmap profilePicture = (Bitmap) data.getExtras().get("data");
                //Bitmap ScaledImage = Bitmap.createScaledBitmap(profilePicture, 110, 110, false);
                //roundImage = new RoundImage(ScaledImage);
                ivProfilePicture.setImageBitmap(profilePicture);

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] myProjection = {MediaStore.MediaColumns.DATA};
                Cursor myCursor = getContentResolver().query(selectedImageUri, myProjection, null, null, null);

                int column_index = myCursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                myCursor.moveToFirst();

                String selectedImagePath = myCursor.getString(column_index);

                Bitmap profilePicture;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;

                while (options.outWidth / scale / 2 >= REQUIRED_SIZE &&
                        options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                profilePicture = BitmapFactory.decodeFile(selectedImagePath, options);
                //Bitmap ScaledImage = Bitmap.createScaledBitmap(profilePicture, 110, 110, false);
                //roundImage = new RoundImage(ScaledImage);
                ivProfilePicture.setImageBitmap(profilePicture);
            }
        }
    }

    public void selectFromSpinner(){
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
    */


}
