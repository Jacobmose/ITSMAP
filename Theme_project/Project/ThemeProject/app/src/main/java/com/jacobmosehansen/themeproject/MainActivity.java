package com.jacobmosehansen.themeproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;

import com.jacobmosehansen.themeproject.Chat.ChatListActivity;
import com.jacobmosehansen.themeproject.Post.NewPostActivity;
import com.jacobmosehansen.themeproject.Post.PostsActivity;
import com.jacobmosehansen.themeproject.Profile.ProfileActivity;

import com.jacobmosehansen.themeproject.Profile.UserProfile;
import com.jacobmosehansen.themeproject.Tools.DBUserAdapter;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    Button btn_posts, btn_chat, btn_newPost, btn_profile, btn_search;
    EditText etSearch;
    ListAdapter mListAdapter;
    Spinner sprSearch;
    Integer userId;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> userIdHolder;
    UserProfile userProfile =  new UserProfile();
    DBUserAdapter dbUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSavedPreferences();

        btn_posts = (Button) findViewById(R.id.btn_posts);
        btn_chat = (Button) findViewById(R.id.btn_chat);
        btn_newPost = (Button) findViewById(R.id.btn_newPost);
        btn_profile = (Button) findViewById(R.id.btn_profile);
        btn_search = (Button) findViewById(R.id.btn_search);
        etSearch = (EditText) findViewById(R.id.et_search);
        sprSearch = (Spinner) findViewById(R.id.spinner_search);



        Log.d("userId", userId.toString());
        // TEST to verify the correct user ID is saved in SharedPreferences
        dbUser = new DBUserAdapter(MainActivity.this);
        userProfile = dbUser.getUserProfile(userId);
        String name = userProfile.getName();
        userIdHolder = new ArrayList<String>();
        userIdHolder.add(name);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, userIdHolder);
        sprSearch.setAdapter(arrayAdapter);
        // TEST

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
                intent.putExtra("PARSE_ID", userProfile.getParseId());
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
                intent.putExtra("USER_ID", userId);
                startActivityForResult(intent, 1);
            }
        });


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //_TODO Remove this! - made for testing//
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                Integer anotherId = 10;
                intent.putExtra("USER_ID", anotherId);
                startActivityForResult(intent, 1);
                //Remove this! - made for testing//

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
