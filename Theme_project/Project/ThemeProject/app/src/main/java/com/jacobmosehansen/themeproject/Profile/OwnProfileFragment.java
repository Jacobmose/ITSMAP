package com.jacobmosehansen.themeproject.Profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.jacobmosehansen.themeproject.R;

import com.jacobmosehansen.themeproject.Tools.DBUserAdapter;
import com.jacobmosehansen.themeproject.Tools.NothingSelectedSpinnerAdapter;
import com.jacobmosehansen.themeproject.Tools.SwipeDismissListViewTouchListener;

import org.w3c.dom.Text;

import java.util.ArrayList;



/**
 * Created by Morten on 06-10-2015.
 */
public class OwnProfileFragment extends Fragment
{
    // UI variables //
    ImageView ivProfilePicture;
    TextView tvFullName, tvEmail, tvAge, tvGender, tvLocation;
    RatingBar rbGradRating;
    Spinner sprSubjects;
    Button btnAddSubject;
    ArrayList<String> subjectArray = new ArrayList<String>();
    private ArrayAdapter<String> mySubjectAdapter;
    ListView lvSubjects;


    // DB variables //
    Integer userId;
    SharedPreferences mySharedPreferences;
    UserProfile userProfile =  new UserProfile();


    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_FILE = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_profile_own, container, false);
        loadSavedPreferences();

        // Load current profile //
        DBUserAdapter dbUserAdapter = new DBUserAdapter(getActivity());
        userProfile = dbUserAdapter.getUserProfile(userId);

        // Initialize Views//
        ivProfilePicture = (ImageView) myFragmentView.findViewById(R.id.imageView_profilePicture);

        tvFullName = (TextView) myFragmentView.findViewById(R.id.tv_fullName);
        tvEmail = (TextView) myFragmentView.findViewById(R.id.tv_email);
        tvAge = (TextView) myFragmentView.findViewById(R.id.tv_age);
        tvGender = (TextView) myFragmentView.findViewById(R.id.tv_gender);
        tvLocation = (TextView) myFragmentView.findViewById(R.id.tv_location);

        rbGradRating = (RatingBar) myFragmentView.findViewById(R.id.ratingBar_profileRating);

        sprSubjects = (Spinner) myFragmentView.findViewById(R.id.spinner_subjects);
        btnAddSubject = (Button) myFragmentView.findViewById(R.id.btn_addSubject);
        lvSubjects = (ListView) myFragmentView.findViewById(R.id.lv_subjects);
        mySubjectAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, subjectArray);


        // _REMOVE TEST for own profile id//
        Toast.makeText(getActivity(), userId.toString(), Toast.LENGTH_SHORT).show();

        // Set textView's with database information //
        tvFullName.setText(userProfile.getName());
        tvEmail.setText(userProfile.getEmail());
        tvAge.setText(userProfile.getAge());
        tvGender.setText(userProfile.getGender());
        //_TODO LOCATION tvLocation.setText(userProfile.getLocation());

        // _TODO Set picture with database information //

        // _TODO Set ratingbar with database information//

        // _TODO Set list view with database information//

        // On profile picture press, open camera and take picture for imageView //
        ivProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                // _TODO SAVE PICTURE TO DATABASE!!!!!!!!!!!!!!!!!!!!!
                // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            }
        });

        // Set Subject Spinner //
        final ArrayAdapter<CharSequence> myAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.subjects_array, android.R.layout.simple_spinner_item);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // Set Subject Selection Preset Text //
        sprSubjects.setAdapter(new NothingSelectedSpinnerAdapter(myAdapter,
                R.layout.spinner_row_nothing_selected, getActivity()));


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
                                //_TODO REMOVE FROM SUBJECT//
                                mySubjectAdapter.notifyDataSetChanged();
                            }
                        });
        lvSubjects.setOnTouchListener(touchListener);

        return myFragmentView;
    }

    // Note by Morten: //
    // This function was inspired by the tutorial http://www.theappguruz.com/blog/android-take-photo-camera-gallery-code-sample//
    // Function makes it posible for user to either take new picture or select a picture from the library for profile picture//
    private void selectImage(){
        final CharSequence[] options = {"Take new photo", "Choose photo from library", "Cancel"};

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == CAMERA_REQUEST) {
                Bitmap profilePicture = (Bitmap) data.getExtras().get("data");
                //_TODO Bitmap ScaledImage = Bitmap.createScaledBitmap(profilePicture, 110, 110, false);
                //roundImage = new RoundImage(ScaledImage);
                ivProfilePicture.setImageBitmap(profilePicture);

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] myProjection = {MediaStore.MediaColumns.DATA};
                Cursor myCursor = getActivity().getContentResolver().query(selectedImageUri, myProjection, null, null, null);

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
                // _TODO Bitmap ScaledImage = Bitmap.createScaledBitmap(profilePicture, 110, 110, false);
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
                    Toast.makeText(getActivity(), "Subject already added", Toast.LENGTH_SHORT).show();
                }else {
                    mySubjectAdapter.add(selectedSubject);
                    lvSubjects.setAdapter(mySubjectAdapter);
                    //_TODO SAVE SUBJECTS TO DATABASE //
                }}
            else{Toast.makeText(getActivity(), "Too many subjects added", Toast.LENGTH_SHORT).show();}
        }catch(Exception e){
            Toast.makeText(getActivity(), "No subject selected", Toast.LENGTH_SHORT).show();
        }
    }


    private void loadSavedPreferences(){
        mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userId = mySharedPreferences.getInt("USER_ID", 0);
    }

}
