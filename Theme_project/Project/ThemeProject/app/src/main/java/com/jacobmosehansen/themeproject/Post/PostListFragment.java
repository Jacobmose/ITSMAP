package com.jacobmosehansen.themeproject.Post;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.jacobmosehansen.themeproject.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marlene on 10-10-2015.
 */
public class PostListFragment extends Fragment {

    private Button btnNewPost;
    private ListView lvwPostList;
    private String userId = ParseUser.getCurrentUser().getObjectId();
    private ArrayList<ParseObject> postList;
    private PostInterface postInterface;
    private PostAdaptor postAdaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View myFragmentView = inflater.inflate(R.layout.post_list, container, false);
        btnNewPost = (Button) myFragmentView.findViewById(R.id.btn_newPost);
        lvwPostList = (ListView) myFragmentView.findViewById(R.id.lvPostList);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParsePost");
        //query.whereNotEqualTo("UserID", userId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e == null){
                    postAdaptor = new PostAdaptor(getActivity(), (ArrayList<ParseObject>)list);
                    lvwPostList.setAdapter(postAdaptor);
                }else{
                    Toast.makeText(getActivity(), "Error: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnNewPost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewPostActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        lvwPostList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                postInterface.onPostSelected(position);
            }
        });


        return myFragmentView;
    }

    public ParseObject getTopic(int pos){
        return postAdaptor.getItem(pos);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        postInterface = (PostInterface) activity;
    }
}
