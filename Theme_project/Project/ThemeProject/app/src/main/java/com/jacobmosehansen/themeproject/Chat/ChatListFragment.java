package com.jacobmosehansen.themeproject.Chat;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jacobmosehansen.themeproject.R;
import com.sinch.android.rtc.messaging.WritableMessage;

import java.util.ArrayList;

public class ChatListFragment extends Fragment {

    private ListView listView;
    private ChatInterface chatInterface;
    private ChatListAdaptor chatListAdaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        listView = (ListView) view.findViewById(R.id.listView3);

        chatListAdaptor = new ChatListAdaptor(getActivity());

        chatInterface.populateMessageList();

        listView.setAdapter(chatListAdaptor);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chatInterface.onChatSelected(position);
            }
        });

        return view;
    }

    public void addChatToList(ChatItem chat){
        chatListAdaptor.addMessage(chat);
    }

    public ChatItem getChat(int i){
        return chatListAdaptor.getItem(i);
    }

    public ArrayList<ChatItem> getChatItems(){
        return chatListAdaptor.getChatItems();
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        chatInterface = (ChatInterface) activity;
    }
}


