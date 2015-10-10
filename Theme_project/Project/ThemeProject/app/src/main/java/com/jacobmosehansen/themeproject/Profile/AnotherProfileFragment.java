package com.jacobmosehansen.themeproject.Profile;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.jacobmosehansen.themeproject.R;
import com.jacobmosehansen.themeproject.Tools.ParseAdapter;
import com.jacobmosehansen.themeproject.Tools.RoundImage;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Morten on 06-10-2015.
 */
public class AnotherProfileFragment extends Fragment {

    // UI variables //
    ImageView ivProfilePicture;
    TextView tvFullName, tvAge, tvGender, tvLocation;
    RatingBar rbGradRating;
    Button btnRating;
    ArrayList<String> subjectArray = new ArrayList<String>();
    private ArrayAdapter<String> mySubjectAdapter;
    ListView lvSubjects;
    RoundImage roundImage;

    // DB variables //
    String requestId;
    ParseUser userProfile;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_profile_another, container, false);

        // Load current profile //
        Bundle idBundle = this.getArguments();
        requestId = idBundle.getString("ID_KEY");

        // Initialize Views//
        ivProfilePicture = (ImageView) myFragmentView.findViewById(R.id.imageView_profilePicture);

        tvFullName = (TextView) myFragmentView.findViewById(R.id.tv_fullName);
        tvAge = (TextView) myFragmentView.findViewById(R.id.tv_age);
        tvGender = (TextView) myFragmentView.findViewById(R.id.tv_gender);
        tvLocation = (TextView) myFragmentView.findViewById(R.id.tv_location);

        rbGradRating = (RatingBar) myFragmentView.findViewById(R.id.ratingBar_profileRating);
        btnRating = (Button) myFragmentView.findViewById(R.id.btn_rating);

        lvSubjects = (ListView) myFragmentView.findViewById(R.id.lv_subjects);
        mySubjectAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, subjectArray);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", requestId);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null) {
                    if (list.isEmpty()) {
                        Log.d("Test", "FAIL no user with id " + requestId);
                    } else {
                        userProfile = list.get(0);
                        if (userProfile.getObjectId() != null) {
                            // Set textView's with database information //
                            tvFullName.setText(userProfile.getUsername());
                            tvAge.setText(userProfile.getString(ParseAdapter.KEY_AGE));
                            tvGender.setText(userProfile.getString(ParseAdapter.KEY_GENDER));
                            //_TODO LOCATION tvLocation.setText(userProfile.getLocation());

                            // Load profile picture
                            loadImageFromDB();

                            // _TODO load subjects
                            loadSubjectFromDB();

                            // Get Ratingbar amount and save onclick //
                            if (userProfile.getNumber(ParseAdapter.KEY_RATINGAMOUNT).intValue() != 0) {
                                rbGradRating.setRating(userProfile.getNumber(ParseAdapter.KEY_RATING).floatValue() / userProfile.getNumber(ParseAdapter.KEY_RATING).intValue());
                            } else {
                                rbGradRating.setRating(0);
                            }
                        }
                    }


                } else {
                    Toast.makeText(getActivity(), "Could not find User", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRating.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // User rating worked on local database, not possible on parse server, since one user
                // Cannot change another users information.
                /*userProfile = dbUserAdapter.getUserProfile(requestId);
                float rating = rbGradRating.getRating();
                float totalRating = Float.parseFloat(userProfile.getRating());
                Integer numberOfRatings = Integer.parseInt(userProfile.getRatingAmount());

                totalRating = totalRating+rating;
                dbUserAdapter.setRating(requestId.toString(), Float.toString(totalRating));

                numberOfRatings = numberOfRatings + 1;
                dbUserAdapter.setRatingAmount(requestId.toString(), Integer.toString(numberOfRatings));

                float currentRating = totalRating/numberOfRatings;

                rbGradRating.setRating(currentRating);*/

                float rating = rbGradRating.getRating();
                float totalRating = userProfile.getNumber(ParseAdapter.KEY_RATING).floatValue();
                Integer numberOfRatings = userProfile.getNumber(ParseAdapter.KEY_RATINGAMOUNT).intValue();

                totalRating = totalRating + rating;
                userProfile.put(ParseAdapter.KEY_RATING, totalRating);
                //dbUserAdapter.setRating(requestId.toString(), Float.toString(totalRating));

                numberOfRatings = numberOfRatings + 1;
                userProfile.put(ParseAdapter.KEY_RATINGAMOUNT, numberOfRatings);
                //dbUserAdapter.setRatingAmount(requestId.toString(), Integer.toString(numberOfRatings));

                float currentRating = totalRating / numberOfRatings;

                rbGradRating.setRating(currentRating);

                userProfile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d("Test", "succes");
                        } else {
                            Log.d("Test", "failed " + e.getLocalizedMessage());
                        }
                    }
                });
            }
        });



        return myFragmentView;
    }

    private void loadImageFromDB() {
        try {
            final ParseFile profilePicture = (ParseFile) userProfile.get(ParseAdapter.KEY_PICTURE);
            profilePicture.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    if (e == null) {
                        Log.d("Debug", "Picture received");
                        Bitmap picture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                        roundImage = new RoundImage(picture);
                        ivProfilePicture.setImageDrawable(roundImage);
                    } else {
                        Log.d("Debug", "Something went wrong");
                    }
                }
            });
        } catch (Exception e) {
            try {
                Bitmap defaultPicture = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.default_profile);
                roundImage = new RoundImage(defaultPicture);
                ivProfilePicture.setImageDrawable(roundImage);
            }catch (Exception f) {
                //Log.d("Failed", "failed in default");
            }

        }

    }

    private void loadSubjectFromDB() {
        try{
            ArrayList<String> testStringArrayList = (ArrayList<String>)userProfile.get(ParseAdapter.KEY_SUBJECTS);
            mySubjectAdapter.addAll(testStringArrayList);
            lvSubjects.setAdapter(mySubjectAdapter);
        } catch (Exception e){
            Log.d("Debug", "Array empty");
        }

    }
}


