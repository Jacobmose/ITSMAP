package com.jacobmosehansen.themeproject.Chat;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.jacobmosehansen.themeproject.R;
import com.jacobmosehansen.themeproject.Tools.ParseAdapter;
import com.jacobmosehansen.themeproject.Tools.RoundImage;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sinch.android.rtc.messaging.WritableMessage;

import java.util.List;

public class ChatWindowFragment extends Fragment {

    private TextView txtTopic;
    private TextView txtPerson;
    private ImageView imgPerson;
    private ListView lvwMessages;
    private EditText etxMessage;
    private Button btnSend;
    private String message;
    private RoundImage roundImage;
    private MessageAdapter messageAdapter;
    private String RecipientId;
    private ChatInterface chatInterface;
    private ParseUser RecipientUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_window, container, false);

        txtPerson = (TextView) view.findViewById(R.id.txtChatWindowPerson);
        txtTopic = (TextView) view.findViewById(R.id.txtChatWindowTopic);
        imgPerson = (ImageView) view.findViewById(R.id.imvChatWindowImage);
        etxMessage = (EditText) view.findViewById(R.id.etxChatWindow);
        btnSend = (Button) view.findViewById(R.id.btnChatWindow);
        lvwMessages = (ListView) view.findViewById(R.id.lvwChatWindow);

        chatInterface.populateMessageHistory(RecipientId);

        messageAdapter = new MessageAdapter(getActivity());

        lvwMessages.setAdapter(messageAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = etxMessage.getText().toString();
                if(message != ""){
                    chatInterface.sendMessage(RecipientId, message);
                }
            }
        });
        return view;
    }

    public void setRecipient(String Recipient){
        RecipientId = Recipient;
    }

    public void setChat(String recipientId)
    {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", recipientId);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if(e == null) {
                    if (!list.isEmpty()) {
                        RecipientUser = list.get(0);
                        if (RecipientUser != null) {

                            txtPerson.setText(RecipientUser.getUsername());
                            //TODO set Topic
                            txtTopic.setText("Topic");

                            ParseFile profilePicture = (ParseFile) RecipientUser.get(ParseAdapter.KEY_PICTURE);
                            profilePicture.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] bytes, ParseException e) {
                                    if (e == null) {
                                        Log.d("Debug", "Picture received");
                                        Bitmap picture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                        roundImage = new RoundImage(picture);
                                        imgPerson.setImageDrawable(roundImage);
                                    } else {
                                        Log.d("Debug", "Something went wrong");
                                    }
                                }
                            });

                        }
                    }
                }else{
                    Toast.makeText(getActivity(), "Could not find User", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addMessageToList(WritableMessage message, int direction){
        messageAdapter.addMessage(message, direction);
    }

    public String getRecipientId(){
        return RecipientId;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        chatInterface = (ChatInterface) activity;
    }
}
