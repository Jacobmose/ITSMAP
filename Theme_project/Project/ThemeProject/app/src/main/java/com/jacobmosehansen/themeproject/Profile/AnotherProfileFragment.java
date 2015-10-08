package com.jacobmosehansen.themeproject.Profile;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jacobmosehansen.themeproject.R;

/**
 * Created by Morten on 06-10-2015.
 */
public class AnotherProfileFragment extends Fragment {

    Integer requestId;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_profile_own, container, false);

        Bundle idBundle = this.getArguments();
        requestId = idBundle.getInt("ID_KEY");


        //Test for requested ID//
        Toast.makeText(getActivity(), requestId.toString(), Toast.LENGTH_SHORT).show();


        return myFragmentView;
    }
}
