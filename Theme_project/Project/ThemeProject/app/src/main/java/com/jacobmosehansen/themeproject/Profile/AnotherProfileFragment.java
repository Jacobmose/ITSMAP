package com.jacobmosehansen.themeproject.Profile;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.jacobmosehansen.themeproject.Tools.DBUserAdapter;

import java.util.ArrayList;

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

    // DB variables //
    Integer requestId;
    UserProfile userProfile =  new UserProfile();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_profile_another, container, false);

        // Load current profile //
        Bundle idBundle = this.getArguments();
        requestId = idBundle.getInt("ID_KEY");
        DBUserAdapter dbUserAdapter = new DBUserAdapter(getActivity());
        userProfile = dbUserAdapter.getUserProfile(requestId);

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

        // _REMOVE Test for requested ID//
        Toast.makeText(getActivity(), userProfile.getRating().toString(), Toast.LENGTH_SHORT).show();

        // Set textView's with database information //
        tvFullName.setText(userProfile.getName());
        tvAge.setText(userProfile.getAge());
        tvGender.setText(userProfile.getGender());
        //_TODO LOCATION tvLocation.setText(userProfile.getLocation());

        // Get Ratingbar amount and save onclick //
        if(Float.parseFloat(userProfile.getRatingAmount())!=0) {
            rbGradRating.setRating(Float.parseFloat(userProfile.getRating()) / Float.parseFloat(userProfile.getRatingAmount()));
        } else {
            rbGradRating.setRating(0);
        }

        btnRating.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                float rating = rbGradRating.getRating();
                float totalRating = Float.parseFloat(userProfile.getRating());
                Integer numberOfRatings = Integer.parseInt(userProfile.getRatingAmount());

                totalRating = totalRating+rating;
                userProfile.setRating(Float.toString(totalRating));

                numberOfRatings++;
                userProfile.setRatingAmount(Integer.toString(numberOfRatings));

                float currentRating = (float) Math.round((totalRating/numberOfRatings)*2)/2;

                rbGradRating.setRating(currentRating);


                Toast.makeText(getActivity(), Float.toString(currentRating), Toast.LENGTH_SHORT).show();
            }
        });




        return myFragmentView;
    }
}
