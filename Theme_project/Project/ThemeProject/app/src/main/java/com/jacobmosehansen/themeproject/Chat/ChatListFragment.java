package com.jacobmosehansen.themeproject.Chat;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jacobmosehansen.themeproject.R;

public class ChatListFragment extends Fragment {

    private ListView listView;
    private ChatInterface chatInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        listView = (ListView) view.findViewById(R.id.listView3);

        ChatListAdaptor adapter = new ChatListAdaptor(getActivity(), chatInterface.getChatList());

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chatInterface.onChatSelected(position);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        chatInterface = (ChatInterface) activity;
    }
}


