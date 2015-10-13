package com.jacobmosehansen.themeproject.Post;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.jacobmosehansen.themeproject.R;
import com.parse.ParseObject;

/**
 * Created by Marlene on 08-10-2015.
 */

    //Skal starte naar der trykkes paa "Posts" fra starten af.
    //Skal starte nr der trykkes p "Posts" fra starten af.
    //Skal hente data omkring hvilke posts der er i DB'en.
    //Skal initiere fragment "details" samt den nye aktivitet "New post".

public class PostsActivity extends AppCompatActivity implements PostInterface {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private PostListFragment postListFragment;
    private PostDetailsFragment postDetailsFragment;
    ParseObject currentTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        postListFragment = new PostListFragment();
        postDetailsFragment = new PostDetailsFragment();
        //PostDetailsFragment postDetailsFragment = new PostDetailsFragment();

        fragmentTransaction.add(android.R.id.content, postListFragment);
        fragmentTransaction.commit();
    }

    public void onPostSelected(int pos){
        currentTopic = postListFragment.getTopic(pos);
        if(currentTopic == null){
            Log.d("ERROR","onPostSelect");
        }
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, postDetailsFragment)
                .commit();
    }

    public ParseObject onGetSelectedTopic(){
        if(currentTopic == null)
        {
            Log.d("ERROR","onGetSelected");
        }
        return currentTopic;
    }

    /*private void loadImageFromDB() {
        ParseFile profilePicture = (ParseFile)userProfile.get(ParseAdapter.KEY_PICTURE);
        profilePicture.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                if (e == null){
                    Log.d("Debug", "Picture received");
                    Bitmap picture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    roundImage = new RoundImage(picture);
                    imageView.setImageDrawable(roundImage);
                } else {
                    Log.d("Debug", "Something went wrong");
                }
            }
        });
    }*/
}
