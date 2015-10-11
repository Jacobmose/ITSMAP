package com.jacobmosehansen.themeproject.Chat;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.parse.ParseObject;
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
    private ParseObject topic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_window, container, false);

        txtPerson = (TextView) view.findViewById(R.id.txtChatWindowPerson);
        txtTopic = (TextView) view.findViewById(R.id.txtChatWindowTopic);
        imgPerson = (ImageView) view.findViewById(R.id.imvChatWindowImage);
        etxMessage = (EditText) view.findViewById(R.id.etxChatWindow);
        btnSend = (Button) view.findViewById(R.id.btnChatWindow);
        lvwMessages = (ListView) view.findViewById(R.id.lvwChatWindow);

        messageAdapter = new MessageAdapter(getActivity());

        lvwMessages.setAdapter(messageAdapter);

        chatInterface.populateMessageHistory(topic);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etxMessage.getText() != null) {
                    String message = topic.get("topic").toString() + "#-#" + etxMessage.getText().toString();
                    chatInterface.sendMessage(RecipientId, message);
                    etxMessage.setText("");
                    hideSoftKeyboard(getActivity());
                    lvwMessages.scrollTo(0, lvwMessages.getHeight());

                }
            }
        });
        return view;
    }

    public void setChat(ChatItem chat)
    {
        RecipientId = chat.getPersonId();
        txtPerson.setText(chat.getPerson());
        txtTopic.setText(chat.getTopic());
        if(chat.getPersonImg() != null){
            imgPerson.setImageDrawable(chat.getPersonImg());
        }else{
            Bitmap image = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.default_profile);
            roundImage = new RoundImage(image);
            imgPerson.setImageDrawable(roundImage);
        }

        Log.d("TEST", "TEST" + chat.getTopicId());
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseChat");
        query.whereEqualTo("objectId", chat.getTopicId());
        query.include("parseMessage");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (!list.isEmpty()) {
                    topic = list.get(0);
                    chatInterface.populateMessageHistory(topic);
                } else {
                    Log.d("TEST", "FAILED");
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

    public ParseObject getTopic(){
        return topic;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        chatInterface = (ChatInterface) activity;
    }
    //From http://stackoverflow.com/questions/4165414/how-to-hide-soft-keyboard-on-android-after-clicking-outside-edittext
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
