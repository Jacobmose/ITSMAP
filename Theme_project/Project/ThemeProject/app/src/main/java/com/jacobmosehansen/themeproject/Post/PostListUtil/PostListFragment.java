package com.jacobmosehansen.themeproject.Post.PostListUtil;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.jacobmosehansen.themeproject.Post.Adapter.PostListAdapter;
import com.jacobmosehansen.themeproject.Post.PostListUtil.PostDetailFragment;
import com.jacobmosehansen.themeproject.Post.dummy.DummyContent;
import com.jacobmosehansen.themeproject.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Marlene on 08-10-2015.
 */
public class PostListFragment extends Fragment {

    private ListView postListView;
    private PostListAdapter adapter;
    private ArrayList<Post> posts;

    private ImageView imgProfil;

    private PostSelectorInterface postSelector;

    public PostListFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_post_list, container, false);

        postListView = (ListView) view.findViewById(R.id.post_list);
        imgProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(postSelector!=null){
                    postSelector.viewSpecial();
                }
            }
        });
        updatePost();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            postSelector = (PostSelectorInterface) activity;
        } catch (ClassCastException ex) {
            throw new ClassCastException(activity.toString() + "PostSelectorInterface skal inplementeres");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = android.support.v4.app.Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void onPostSelected(int position){
        if(postSelector!=null) {
            postSelector.onPostSelected(position);
        }
    }

    public void setMovies(ArrayList<Post> postList){
        posts = (ArrayList<Post>) postList.clone();
    }

    public void updatePost(){
        if(postSelector!=null){
            posts = postSelector.getPostList();
        }
        if(posts!=null) {
            adapter = new PostListAdapter(getActivity(), posts);
            postListView.setAdapter(adapter);

            postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onPostSelected(position);
                }
            });
        }
    }
}
