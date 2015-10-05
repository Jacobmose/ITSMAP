package com.jacobmosehansen.themeproject.Chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jacobmosehansen.themeproject.R;

public class ChatListActivity extends AppCompatActivity implements ChatWindowFragment.OnItemSelectedListener  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
    }

    @Override
    public void onRssItemSelected(String link) {
        ChatListFragment fragment = (ChatListFragment) getFragmentManager()
                .findFragmentById(R.id.detailFragment);
        if (fragment != null && fragment.isInLayout()) {
            fragment.setText(link);
        }
    }
}
