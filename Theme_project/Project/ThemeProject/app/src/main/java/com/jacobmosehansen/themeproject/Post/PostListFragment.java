package com.jacobmosehansen.themeproject.Post;

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

import com.jacobmosehansen.themeproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marlene on 10-10-2015.
 */
public class PostListFragment extends Fragment {

    private ListView listView;
    List<Post> _postItems;
    //private PostInterface mPostInterface;

    private String[] _postTitles = new String[]{};
    private String[] _postNames = new String[]{};
    private String[] _postSubjects = new String[]{};
    private String[] _postLevels = new String[]{};
    private Drawable[] _postImages = {};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.post_list, container, false);

        _postItems = new ArrayList<Post>();

        for (int i = 0; i < _postTitles.length; i++){
            Post post = new Post(_postImages[i], _postTitles[i], _postNames[i], _postSubjects[i], _postLevels[i]);
            _postItems.add(post);
        }

        listView = (ListView) view.findViewById(R.id.lvPostList);


        // THIS IS NOT RIGHT (SHOULD USE THE _postItems LIST TO PARAMETERIZE THE ADAPTOR
        //PostAdaptor adaptor = new PostAdaptor(getActivity(), mPostInterface.getPostList());

        //listView.setAdapter(adaptor);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // DO WHAT IS SUPPOSED TO HAPPEN WHEN POST IS CLICKED
            }
        });

        Button button = (Button) view.findViewById(R.id.btn_newPost);


        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewPostActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        return view;
    }
}
