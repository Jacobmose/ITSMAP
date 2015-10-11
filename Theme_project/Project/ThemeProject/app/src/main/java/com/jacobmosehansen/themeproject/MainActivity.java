package com.jacobmosehansen.themeproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;

import com.jacobmosehansen.themeproject.Chat.ChatActivity;
import com.jacobmosehansen.themeproject.Login.LoginActivity;
import com.jacobmosehansen.themeproject.Post.NewPostActivity;
import com.jacobmosehansen.themeproject.Post.PostsActivity;
import com.jacobmosehansen.themeproject.Profile.ProfileActivity;

//import com.jacobmosehansen.themeproject.Profile.UserProfile;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    Button btn_posts, btn_chat, btn_newPost, btn_profile, btn_search, btn_logOut;
    EditText etSearch;
    ListAdapter mListAdapter;
    Spinner sprSearch;
    Integer userId;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> userIdHolder;
    //UserProfile userProfile =  new UserProfile();
    //DBUserAdapter dbUser;
    ParseUser currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = ParseUser.getCurrentUser();

        loadSavedPreferences();

        btn_posts = (Button) findViewById(R.id.btn_posts);
        btn_chat = (Button) findViewById(R.id.btn_chat);
        btn_newPost = (Button) findViewById(R.id.btn_newPost);
        btn_profile = (Button) findViewById(R.id.btn_profile);
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_logOut = (Button) findViewById(R.id.btn_logout);
        etSearch = (EditText) findViewById(R.id.et_search);
        sprSearch = (Spinner) findViewById(R.id.spinner_search);

        /*// TEST to verify the correct user ID is saved in SharedPreferences
        dbUser = new DBUserAdapter(MainActivity.this);
        userProfile = dbUser.getUserProfile(userId);
        String name = userProfile.getName();
        userIdHolder = new ArrayList<String>();
        userIdHolder.add(name);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, userIdHolder);
        sprSearch.setAdapter(arrayAdapter);
        // TEST*/

        btn_posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PostsActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        btn_newPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewPostActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("USER_ID", currentUser.getObjectId());
                startActivityForResult(intent, 1);
            }
        });


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //_TODO Remove this! - made for testing//
                /*Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("USER_ID", "MmDjqfcqR7");
                startActivityForResult(intent, 1);*/
                //Remove this! - made for testing//

                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("RECIPIENT_ID", "MmDjqfcqR7");
                intent.putExtra("TOPIC_ID", "Topic Test");
                startActivity(intent);

            }
        });

        btn_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();

                currentUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    public ArrayList<String> getList(String search){

        ArrayList<String> results = new ArrayList<String>();

        try {
            Cursor mCursor = db.rawQuery("SELECT * FROM users WHERE username || ' ' || email LIKE ?", new String[]{"%" + etSearch.getText().toString() + "%"});

            if (mCursor != null) {
                mCursor.moveToFirst();
                for (int i = 0; i < mCursor.getCount(); i++) {
                    results.add(mCursor.getString(0));
                    mCursor.moveToNext();
                }
                mCursor.close();
                return results;
            }
            return results;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    private void loadSavedPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getInt("USER_ID", 0);
    }
}
