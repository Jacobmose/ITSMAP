package dk.iha.itsmap.e15.grp03.studybuddy.Profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
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

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import dk.iha.itsmap.e15.grp03.studybuddy.R;
import dk.iha.itsmap.e15.grp03.studybuddy.Tools.NothingSelectedSpinnerAdapter;
import dk.iha.itsmap.e15.grp03.studybuddy.Tools.ParseAdapter;
import dk.iha.itsmap.e15.grp03.studybuddy.Tools.RoundImage;
import dk.iha.itsmap.e15.grp03.studybuddy.Tools.SwipeDismissListViewTouchListener;


/**
 * Created by Morten on 06-10-2015.
 */
public class OwnProfileFragment extends Fragment
{
    // UI variables //
    ImageView ivProfilePicture;
    TextView tvUserName, tvEmail, tvAge, tvGender, tvLocation, tvNumberOfRatings;
    RatingBar rbGradRating;
    Spinner sprSubjects;
    Button btnAddSubject;
    ArrayList<String> subjectArray = new ArrayList<String>();
    private ArrayAdapter<String> mySubjectAdapter;
    ListView lvSubjects;
    RoundImage roundImage;


    // DB variables //
    String userId;
    ParseUser userProfile = ParseUser.getCurrentUser();
    ParseFile file;
    ParseObject ratingObject;



    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_FILE = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_profile_own, container, false);
        // Get userId //
        userId = ParseUser.getCurrentUser().getObjectId();

        // Initialize Views//
        ivProfilePicture = (ImageView) myFragmentView.findViewById(R.id.imageView_profilePicture);

        tvUserName = (TextView) myFragmentView.findViewById(R.id.tv_userName);
        tvEmail = (TextView) myFragmentView.findViewById(R.id.tv_email);
        tvAge = (TextView) myFragmentView.findViewById(R.id.tv_age);
        tvGender = (TextView) myFragmentView.findViewById(R.id.tv_gender);
        tvLocation = (TextView) myFragmentView.findViewById(R.id.tv_location);

        rbGradRating = (RatingBar) myFragmentView.findViewById(R.id.ratingBar_profileRating);
        tvNumberOfRatings = (TextView) myFragmentView.findViewById(R.id.tv_numberOfRatings);

        sprSubjects = (Spinner) myFragmentView.findViewById(R.id.spinner_subjects);
        btnAddSubject = (Button) myFragmentView.findViewById(R.id.btn_addSubject);
        lvSubjects = (ListView) myFragmentView.findViewById(R.id.lv_subjects);
        mySubjectAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, subjectArray);

        final String numberOfSubjectsTxt = getResources().getString(R.string.numberOfSubjects_text);

        // Set textView's with database information //
        tvUserName.setText(userProfile.getUsername());
        tvEmail.setText(userProfile.getEmail());
        tvAge.setText(userProfile.getString(ParseAdapter.KEY_AGE) + getResources().getString(R.string.userYearsOld_text));
        tvGender.setText(userProfile.get(ParseAdapter.KEY_GENDER).toString());

        // Set location //
        if(userProfile.get(ParseAdapter.KEY_LOCATION) != null){
            tvLocation.setText(getResources().getString(R.string.lastLogin_text) + userProfile.get(ParseAdapter.KEY_LOCATION).toString());
        } else {
            tvLocation.setText(getResources().getString(R.string.unkownLocation_text));
        }

        // Set picture with database information //
        loadImageFromDB();

        // Set ratingbar with database information//
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ratingObject");
        query.whereEqualTo("userid", userProfile.getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (list.size() != 0){
                    ratingObject = list.get(0);
                    if (ratingObject.getObjectId() != null) {
                        if (ratingObject.getNumber(ParseAdapter.KEY_NUMBEROFRATINGS).intValue() != 0) {
                            Log.d("Debug", "NUMBER OF RATING != 0");
                            rbGradRating.setRating(ratingObject.getNumber(ParseAdapter.KEY_TOTALRATING).floatValue() / ratingObject.getNumber(ParseAdapter.KEY_NUMBEROFRATINGS).intValue());
                        } else {
                        Log.d("Debug", "NUMBER OF RATING = 0");
                        rbGradRating.setRating(0);
                        }
                }else{
                    Log.d("Debug", "LIST IS EMPTY");
                    rbGradRating.setRating(0);
                }
            }tvNumberOfRatings.setText("(" + ratingObject.getNumber(ParseAdapter.KEY_NUMBEROFRATINGS).toString() + numberOfSubjectsTxt + ")");
        }});

        // Set list view with database information//
        loadSubjectFromDB();

        // On profile picture press, open camera and take picture for imageView //
        ivProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
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
        setSwipeActive();

        return myFragmentView;
    }

    // This code is based on the Android-SwipeToDismiss example of use of the
    // SwipeDissmissListViewTouchListener class//
    private void setSwipeActive(){

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
                                saveSubjectsToDB();
                                mySubjectAdapter.notifyDataSetChanged();
                            }
                        });
        lvSubjects.setOnTouchListener(touchListener);
    }

    // Note by Morten: //
    // This function was inspired by the tutorial http://www.theappguruz.com/blog/android-take-photo-camera-gallery-code-sample//
    // Function makes it possible for user to either take new picture or select a picture from the library for profile picture//
    private void selectImage(){
        final CharSequence[] optionsCamera = {getResources().getString(R.string.takePhoto_text),
                getResources().getString(R.string.choosePhoto_text), getResources().getString(R.string.cancelDialog_text)};
        final CharSequence[] optionsNoCamera = {getResources().getString(R.string.choosePhoto_text),
                getResources().getString(R.string.cancelDialog_text)};
        //
        final PackageManager packageManager = getActivity().getPackageManager();
        //
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle(getResources().getString(R.string.dialogTitle_text));

        if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            dialogBuilder.setItems(optionsCamera, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (optionsCamera[item].equals(getResources().getString(R.string.takePhoto_text))) {
                        Intent myPhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(myPhotoIntent, CAMERA_REQUEST);
                    } else if (optionsCamera[item].equals(getResources().getString(R.string.choosePhoto_text))) {
                        Intent myPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        myPhotoIntent.setType("image/*");
                        startActivityForResult(Intent.createChooser(myPhotoIntent, "Select File"), SELECT_FILE);
                    } else if (optionsCamera[item].equals(getResources().getString(R.string.cancelDialog_text))) {
                        dialog.dismiss();
                    }
                }
            });
        } else {
            dialogBuilder.setItems(optionsNoCamera, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (optionsNoCamera[item].equals(getResources().getString(R.string.choosePhoto_text))) {
                        Intent myPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        myPhotoIntent.setType("image/*");
                        startActivityForResult(Intent.createChooser(myPhotoIntent, "Select File"), SELECT_FILE);
                    } else if (optionsNoCamera[item].equals(getResources().getString(R.string.cancelDialog_text))) {
                        dialog.dismiss();
                    }
                }
            });
        }
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
                Bitmap croppedPicture = cropImage(profilePicture);
                saveImageToDB(croppedPicture);

                roundImage = new RoundImage(croppedPicture);
                ivProfilePicture.setImageDrawable(roundImage);
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

                Bitmap croppedPicture = cropImage(profilePicture);

                saveImageToDB(croppedPicture);

                roundImage = new RoundImage(croppedPicture);
                ivProfilePicture.setImageDrawable(roundImage);
            }
        }
    }

    // Function for cropping picture to a square, no matter if height>width or width>height.
    // If else found on http://stackoverflow.com/questions/6908604/android-crop-center-of-bitmap
    private Bitmap cropImage(Bitmap image){
        Bitmap croppedPicture;
        if(image.getWidth() >= image.getHeight()) {
            croppedPicture = Bitmap.createBitmap(image,
                    image.getWidth()/2 - image.getHeight()/2, 0,
                    image.getHeight(), image.getHeight());
        }else{
            croppedPicture = Bitmap.createBitmap(image, 0,
                    image.getHeight()/2 - image.getWidth()/2,
                    image.getWidth(), image.getWidth());
        }
        return croppedPicture;
    }

    private void saveImageToDB(Bitmap picture) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();

        String name = getResources().getString(R.string.pictureName_text) + userId + ".png";

        file = new ParseFile(name, image);

        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    userProfile.put(ParseAdapter.KEY_PICTURE, file);
                    userProfile.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d("TEST", "Success @ saveImageToDB");

                            } else {
                                Log.d("Test", "Failed @ saveImageToDB" + e.getLocalizedMessage());
                            }
                        }
                    });
                } else {
                    Log.d("TEST", "Failed create file @ saveImageToDB");
                }

            }
        });
    }

    private void loadImageFromDB() {
        try {
            final ParseFile profilePicture = (ParseFile) userProfile.get(ParseAdapter.KEY_PICTURE);
            profilePicture.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    if (e == null) {
                        Log.d("Debug", "Picture received @ loadImageFromDB");
                        Bitmap picture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                        roundImage = new RoundImage(picture);
                        ivProfilePicture.setImageDrawable(roundImage);
                    } else {
                        Log.d("Debug", "Something went wrong @ loadImageFromDB");
                    }
                }
            });
        } catch (Exception e) {
            Bitmap defaultPicture = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.default_profile);
            roundImage = new RoundImage(defaultPicture);
            ivProfilePicture.setImageDrawable(roundImage);
        }

    }

    private void saveSubjectsToDB() {
        userProfile.put(ParseAdapter.KEY_SUBJECTS, subjectArray);
        userProfile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("TEST", "Success @ saveSubjectsToDB");
                } else {
                    Log.d("Test", "Failed" + e.getLocalizedMessage());
                }
            }
        });
    }

    private void loadSubjectFromDB() {
        try{
            ArrayList<String> testStringArrayList = (ArrayList<String>)userProfile.get(ParseAdapter.KEY_SUBJECTS);
            mySubjectAdapter.addAll(testStringArrayList);
            lvSubjects.setAdapter(mySubjectAdapter);
        } catch (Exception e){
            Log.d("Debug", "Array empty @ loadSubjectFromDB");
        }

    }


    public void selectFromSpinner(){
        try{
            String selectedSubject = sprSubjects.getSelectedItem().toString();

            if(subjectArray.size() < 5){
                if(subjectArray.contains(selectedSubject)){
                    Toast.makeText(getActivity(), getResources().getString(R.string.subjectAlreadyAdded_text),
                            Toast.LENGTH_SHORT).show();
                }else {
                    mySubjectAdapter.add(selectedSubject);
                    lvSubjects.setAdapter(mySubjectAdapter);
                    saveSubjectsToDB();
                }}
            else{Toast.makeText(getActivity(), getResources().getString(R.string.subjectTooManyAdded_text),
                    Toast.LENGTH_SHORT).show();}
        }catch(Exception e){
            Toast.makeText(getActivity(), getResources().getString(R.string.noSubjectSelected_text), Toast.LENGTH_SHORT).show();
        }
    }
}
