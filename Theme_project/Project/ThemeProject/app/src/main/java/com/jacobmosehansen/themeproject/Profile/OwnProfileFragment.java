package com.jacobmosehansen.themeproject.Profile;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jacobmosehansen.themeproject.R;

/**
 * Created by Morten on 06-10-2015.
 */
public class OwnProfileFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_own, container, false);


        
    }
}
