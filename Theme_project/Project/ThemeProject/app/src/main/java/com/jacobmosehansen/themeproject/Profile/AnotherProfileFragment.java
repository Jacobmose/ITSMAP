package com.jacobmosehansen.themeproject.Profile;

import android.app.Fragment;
import android.content.pm.PackageManager;
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
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Morten on 06-10-2015.
 */
public class AnotherProfileFragment extends Fragment {

    // UI variables //
    ImageView ivProfilePicture;
    TextView tvFullName, tvAge, tvGender, tvLocation, tvUsersSubjects, tvNumberOfRatings;
    RatingBar rbGradRating;
    Button btnRating;
    ArrayList<String> subjectArray = new ArrayList<String>();
    private ArrayAdapter<String> mySubjectAdapter;
    ListView lvSubjects;
    RoundImage roundImage;


    private String age;

    // DB variables //
    String requestId;
    ParseUser userProfile;
    ParseObject ratingObject;


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
        tvUsersSubjects = (TextView) myFragmentView.findViewById(R.id.tv_usersSubjects);

        rbGradRating = (RatingBar) myFragmentView.findViewById(R.id.ratingBar_profileRating);
        btnRating = (Button) myFragmentView.findViewById(R.id.btn_rating);
        tvNumberOfRatings = (TextView) myFragmentView.findViewById(R.id.tv_numberOfRatings);

        lvSubjects = (ListView) myFragmentView.findViewById(R.id.lv_subjects);
        mySubjectAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, subjectArray);

        // Fetch resources //
        final String userYearsOld = getResources().getString(R.string.userYearsOld_text);
        final String lastLogin = getResources().getString(R.string.lastLogin_text);
        final String unknownLocation = getResources().getString(R.string.unkownLocation_text);
        final String anotherUserSubjects = getResources().getString(R.string.anotherUsersSubjects_text);
        final String numberOfSubjectsTxt = getResources().getString(R.string.numberOfSubjects_text);

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
                            tvAge.setText(userProfile.getString(ParseAdapter.KEY_AGE) + userYearsOld);
                            tvGender.setText(userProfile.getString(ParseAdapter.KEY_GENDER));
                            // Set location //
                            if (userProfile.get(ParseAdapter.KEY_LOCATION) != null) {
                                tvLocation.setText(lastLogin + userProfile.get(ParseAdapter.KEY_LOCATION).toString());
                            } else {
                                tvLocation.setText(unknownLocation);
                            }
                            // Set users subjects text //
                            tvUsersSubjects.setText(userProfile.getUsername() + anotherUserSubjects);

                            // Load profile picture
                            loadImageFromDB();

                            // load subjects
                            loadSubjectFromDB();
                            Log.d("Test", "Loaded subjects");

                            ParseQuery<ParseObject> query = ParseQuery.getQuery("ratingObject");
                            query.whereEqualTo("userid", userProfile.getObjectId());
                            query.findInBackground(new FindCallback<ParseObject>() {
                                                       @Override
                                                       public void done(List<ParseObject> list, ParseException e) {
                                                           if (list.size() != 0) {
                                                               ratingObject = list.get(0);
                                                               if (ratingObject.getObjectId() != null) {
                                                                   // Set ratingbar with database information//
                                                                   if (ratingObject.getNumber(ParseAdapter.KEY_NUMBEROFRATINGS).intValue() != 0) {
                                                                       Log.d("Debug", "NUMBER OF RATING != 0");
                                                                       rbGradRating.setRating(ratingObject.getNumber(ParseAdapter.KEY_TOTALRATING).floatValue() / ratingObject.getNumber(ParseAdapter.KEY_NUMBEROFRATINGS).intValue());
                                                                   } else {
                                                                       Log.d("Debug", "NUMBER OF RATING = 0");
                                                                       rbGradRating.setRating(0);
                                                                   }
                                                                   tvNumberOfRatings.setText("(" + ratingObject.getNumber(ParseAdapter.KEY_NUMBEROFRATINGS).toString() + numberOfSubjectsTxt + ")");
                                                                   btnRating.setOnClickListener(new View.OnClickListener() {
                                                                       public void onClick(View v) {
                                                                           float rating = rbGradRating.getRating();
                                                                           float totalRating = ratingObject.getNumber(ParseAdapter.KEY_TOTALRATING).floatValue();
                                                                           Integer numberOfRatings = ratingObject.getNumber(ParseAdapter.KEY_NUMBEROFRATINGS).intValue();

                                                                           totalRating = totalRating + rating;
                                                                           ratingObject.put(ParseAdapter.KEY_TOTALRATING, totalRating);
                                                                           Log.d("Debug", "Writing " + totalRating + " TOTALRATING");

                                                                           numberOfRatings = numberOfRatings + 1;
                                                                           ratingObject.put(ParseAdapter.KEY_NUMBEROFRATINGS, numberOfRatings);
                                                                           Log.d("Debug", "Writing " + numberOfRatings + " NUMBEROFRATINGS");

                                                                           float currentRating = totalRating / numberOfRatings;

                                                                           rbGradRating.setRating(currentRating);

                                                                           ratingObject.saveInBackground(new SaveCallback() {
                                                                               @Override
                                                                               public void done(ParseException e) {
                                                                                   if (e == null) {
                                                                                       Log.d("Test", "Success @ ratingObject.saveInBackground");
                                                                                   } else {
                                                                                       Log.d("Test", "Failed @ ratingObject.saveInBackground " + e.getLocalizedMessage());
                                                                                   }
                                                                               }
                                                                           });
                                                                       }
                                                                   });


                                                               }
                                                           } else {
                                                               rbGradRating.setRating(0);
                                                           }
                                                       }
                            });
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Could not find User", Toast.LENGTH_SHORT).show();
                }
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
            Log.d("Debug", "Array empty @ loadSubjectFromDB");
        }

    }
}


