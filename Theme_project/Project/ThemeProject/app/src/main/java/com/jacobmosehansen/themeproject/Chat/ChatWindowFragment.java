package com.jacobmosehansen.themeproject.Chat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jacobmosehansen.themeproject.R;
import com.jacobmosehansen.themeproject.Tools.RoundImage;


public class ChatWindowFragment extends Fragment {

    TextView txtTopic;
    TextView txtPerson;
    ImageView imgPerson;
    ListView lvwMessages;

    RoundImage roundImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_window, container, false);

        txtPerson = (TextView) view.findViewById(R.id.txtChatWindowPerson);
        txtTopic = (TextView) view.findViewById(R.id.txtChatWindowTopic);
        imgPerson = (ImageView) view.findViewById(R.id.imvChatWindowImage);

        return view;
    }

    public void setChat(ChatItem chat)
    {
        txtTopic.setText(chat.getTopic());
        txtPerson.setText(chat.getPerson());

        if(chat.getPersonImg() != null){
            imgPerson.setImageDrawable(chat.getPersonImg());
        }else{
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.default_profile);
            roundImage = new RoundImage(bm);
            imgPerson.setImageDrawable(roundImage);
        }

        if(chat.getMessageItems() != null){

        }

    }
}
