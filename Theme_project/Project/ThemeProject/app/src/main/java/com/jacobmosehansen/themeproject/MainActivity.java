package com.jacobmosehansen.themeproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.jacobmosehansen.themeproject.Chat.ChatListActivity;
import com.jacobmosehansen.themeproject.Chat.MessageService;
import com.jacobmosehansen.themeproject.Login.LoginActivity;
import com.jacobmosehansen.themeproject.Post.NewPostActivity;
import com.jacobmosehansen.themeproject.Post.PostsActivity;
import com.jacobmosehansen.themeproject.Profile.ProfileActivity;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    Button btn_posts, btn_chat, btn_newPost, btn_profile, btn_search, btn_logOut;
    EditText etSearch;
    Spinner sprSearch;
    Integer userId;
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
                Intent intent = new Intent(MainActivity.this, ChatListActivity.class);
                startActivity(intent);
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
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("USER_ID", "DTRPHYgnwW");
                startActivityForResult(intent, 1);
                //Remove this! - made for testing//

                /*intent.putExtra("RECIPIENT_ID", "MmDjqfcqR7");
                intent.putExtra("TOPIC_ID", "Topic Test");
                startActivity(intent);
                */
            }
        });

        btn_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                stopService(new Intent(MainActivity.this, MessageService.class));

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

    private void loadSavedPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getInt("USER_ID", 0);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
