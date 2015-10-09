package com.jacobmosehansen.themeproject.Chat;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.jacobmosehansen.themeproject.R;
import com.jacobmosehansen.themeproject.Tools.RoundImage;
import com.sinch.android.rtc.messaging.WritableMessage;


public class ChatWindowFragment extends Fragment {

    TextView txtTopic;
    TextView txtPerson;
    ImageView imgPerson;
    ListView lvwMessages;
    EditText etxMessage;
    Button btnSend;

    String message;
    RoundImage roundImage;
    MessageAdapter messageAdapter;
    String recipient;
    private ChatListInterface chatListInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_window, container, false);

        txtPerson = (TextView) view.findViewById(R.id.txtChatWindowPerson);
        txtTopic = (TextView) view.findViewById(R.id.txtChatWindowTopic);
        imgPerson = (ImageView) view.findViewById(R.id.imvChatWindowImage);
        etxMessage = (EditText) view.findViewById(R.id.etxChatWindow);
        btnSend = (Button) view.findViewById(R.id.btnChatWindow);
        lvwMessages = (ListView) view.findViewById(R.id.lvwChatWindow);

        chatListInterface.populateMessageHistory(recipient);

        messageAdapter = new MessageAdapter(getActivity());

        lvwMessages.setAdapter(messageAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = etxMessage.getText().toString();
                if(message != ""){
                    chatListInterface.sendMessage(recipient, message);
                }
            }
        });
        return view;
    }

    public void setRecipient(String Recipient){
        recipient = Recipient;
    }

    public void setChat(ChatItem chat)
    {
        txtTopic.setText(chat.getTopic());
        txtPerson.setText(chat.getPerson());
        recipient = chat.getPersonId();

        if(chat.getPersonImg() != null){
            imgPerson.setImageDrawable(chat.getPersonImg());
        } else {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.default_profile);
            roundImage = new RoundImage(bm);
            imgPerson.setImageDrawable(roundImage);
        }
    }

    public void addMessageToList(WritableMessage message, int direction){
        messageAdapter.addMessage(message, direction);
    }

    public String getRecipientId(){
        return recipient;
    }

    public String getMessage(){
        return etxMessage.getText().toString();
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        chatListInterface = (ChatListInterface) activity;
    }
}
