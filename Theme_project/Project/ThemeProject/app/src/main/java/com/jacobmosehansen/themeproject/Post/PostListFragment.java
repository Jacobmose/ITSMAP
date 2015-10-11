package com.jacobmosehansen.themeproject.Post;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jacobmosehansen.themeproject.R;

/**
 * Created by Marlene on 10-10-2015.
 */
public class PostListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View myFragmentView = inflater.inflate(R.layout.post_list, container, false);
        Button button = (Button) myFragmentView.findViewById(R.id.btn_newPost);

        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewPostActivity.class);
                startActivityForResult(intent, 1);
            }
        });


        return myFragmentView;
    }




}
