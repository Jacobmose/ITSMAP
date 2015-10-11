package com.jacobmosehansen.themeproject.Post;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jacobmosehansen.themeproject.MainActivity;
import com.jacobmosehansen.themeproject.Profile.ProfileActivity;
import com.jacobmosehansen.themeproject.R;
import com.jacobmosehansen.themeproject.Tools.RoundImage;

/**
 * Created by Marlene on 10-10-2015.
 */
    //Skal vise selve info'en omkring Posts.
    //Skal kunne videresende en til både profil og messenger ved at klikke på hhv. billede eller "Contact"

public class PostDetailsFragment extends Fragment{

    RoundImage roundImage;
    String userID = "";
    Button button;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_show_post, container, false);

        button = (Button) myFragmentView.findViewById(R.id.btn_showProfile);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("USER_ID", userID);
                startActivityForResult(intent, 1);
            }
        });
        return myFragmentView;
    }
}
