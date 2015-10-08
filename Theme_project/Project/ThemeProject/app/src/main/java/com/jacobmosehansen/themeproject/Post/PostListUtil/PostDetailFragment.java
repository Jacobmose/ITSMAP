package com.jacobmosehansen.themeproject.Post.PostListUtil;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jacobmosehansen.themeproject.R;

import java.lang.reflect.Field;

/**
 * A fragment representing a single Post detail screen.
 * This fragment is either contained in a {@link PostListActivity}
 * in two-pane mode (on tablets) or a {@link PostDetailActivity}
 * on handsets.
 */
public class PostDetailFragment extends Fragment {

    private TextView txtNavn;
    private TextView txtOverskrift;
    private TextView txtNiveau;
    private TextView txtFag;
    private EditText txtPost;

    private PostSelectorInterface postSelector;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PostDetailFragment() {
        //Det er nødvendigt med en tom constructor.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //FLate layoutet for dette fragment
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);

        txtNavn = (TextView) view.findViewById(R.id.textView9);
        txtOverskrift = (TextView) view.findViewById(R.id.textView10);
        txtNiveau = (TextView) view.findViewById(R.id.textView11);
        txtFag = (TextView) view.findViewById(R.id.textView12);
        txtPost = (EditText) view.findViewById(R.id.editText4);


        updatePost();

        return view;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        try{
            postSelector = (PostSelectorInterface) activity;
        } catch (ClassCastException ex) {
            throw new ClassCastException(activity.toString() + "PostSelectorInterface skal implementeres.");
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();

        try{
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e){
            throw new RuntimeException(e);
        } catch (IllegalAccessException e){
            throw new RuntimeException(e);
        }
    }

    public void setPost(Post post){
        if (txtNavn!=null && txtOverskrift!=null && txtNiveau!=null && txtFag!=null){
            txtNavn.setText(post.getAuthor());
            txtOverskrift.setText(post.getOverskrift());
            txtNiveau.setText(post.getNiveau());
            txtFag.setText(post.getFag());
            txtPost.setText(post.getPost());
        }
    }

    public void updatePost(){
        if (postSelector!=null){
            setPost(postSelector.getCurrentSelection());
        }
    }

}
