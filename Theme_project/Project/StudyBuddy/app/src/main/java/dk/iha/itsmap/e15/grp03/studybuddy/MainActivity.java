package dk.iha.itsmap.e15.grp03.studybuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import dk.iha.itsmap.e15.grp03.studybuddy.Chat.ChatListActivity;
import dk.iha.itsmap.e15.grp03.studybuddy.Chat.MessageService;
import dk.iha.itsmap.e15.grp03.studybuddy.Login.LoginActivity;
import dk.iha.itsmap.e15.grp03.studybuddy.Post.NewPostActivity;
import dk.iha.itsmap.e15.grp03.studybuddy.Post.PostsActivity;
import dk.iha.itsmap.e15.grp03.studybuddy.Profile.ProfileActivity;

public class MainActivity extends AppCompatActivity {

    Button btn_posts, btn_chat, btn_newPost, btn_profile, btn_logOut;
    Integer userId;
    ImageView img_appLogo;
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
        btn_logOut = (Button) findViewById(R.id.btn_logout);
        img_appLogo = (ImageView) findViewById(R.id.img_appLogo);

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
