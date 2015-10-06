package com.jacobmosehansen.themeproject.Chat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.jacobmosehansen.themeproject.R;

import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity implements ChatListInterface {

    ChatListFragment chatListFragment;
    ChatWindowFragment chatWindowFragment;

    ArrayList<ChatItem> chatItems;

    FrameLayout ChatListContainer;
    FrameLayout ChatWindowContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        ChatListContainer = (FrameLayout) findViewById(R.id.chat_list_container);
        ChatWindowContainer = (FrameLayout) findViewById(R.id.chat_window_container);

        chatListFragment = new ChatListFragment();
        chatWindowFragment = new ChatWindowFragment();

        chatItems = new ArrayList<>();

        chatItems.add(new ChatItem("Topic1", "Person1"));
        chatItems.add(new ChatItem("Topic2", "Person2"));
        chatItems.add(new ChatItem("Topic3", "Person3"));
        chatItems.add(new ChatItem("Topic4", "Person4"));

        getFragmentManager().beginTransaction()
                .add(R.id.chat_list_container, chatListFragment, "CHAT_LIST")
                .add(R.id.chat_window_container, chatWindowFragment, "CHAT_WINDOW")
                .commit();
    }

    public void onChatSelected(int pos) {
        chatWindowFragment.setChat(chatItems.get(pos));
        ChatListContainer.setVisibility(View.GONE);
        ChatWindowContainer.setVisibility(View.VISIBLE);
    }

    public ArrayList<ChatItem> getChatList(){
        return chatItems;
    }
}
