package com.jacobmosehansen.themeproject.Chat;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;
import com.sinch.android.rtc.messaging.WritableMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Idea and functionality from https://www.sinch.com/tutorials/android-messaging-tutorial-using-sinch-and-parse/
public class ChatMessageActivity extends AppCompatActivity {

    private TextView txtTopic;
    private TextView txtPerson;
    private ImageView imgPerson;
    private ListView lvwMessages;
    private EditText etxMessage;
    private Button btnSend;
    private MessageAdapter messageAdapter;
    private ParseUser currentUser;
    private String UserId;
    private boolean messageTrue;
    private RoundImage roundImage;

    private String RecipientId;
    private ParseObject parseChat;
    private String TopicName;
    private ChatItem chat;

    private MessageService.MessageServiceInterface messageServiceInterface;
    private ServiceConnection serviceConnection = new MyServiceConnection();
    private MessageClientListener messageClientListener = new MyMessageClientListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);

        txtPerson = (TextView) findViewById(R.id.txtChatMessagePerson);
        txtTopic = (TextView) findViewById(R.id.txtChatMessageTopic);
        imgPerson = (ImageView) findViewById(R.id.imvChatMessageImage);
        etxMessage = (EditText) findViewById(R.id.etxChatMessage);
        btnSend = (Button) findViewById(R.id.btnChatMessage);
        lvwMessages = (ListView) findViewById(R.id.lvwChatMessage);

        currentUser = ParseUser.getCurrentUser();
        UserId = currentUser.getObjectId();

        messageAdapter = new MessageAdapter(ChatMessageActivity.this);

        lvwMessages.setAdapter(messageAdapter);

        Intent intent = getIntent();
        chat = (ChatItem) intent.getSerializableExtra("CHATITEM");

        setChat(chat);

        //Binds to the service
        bindService(new Intent(this, MessageService.class), serviceConnection, BIND_AUTO_CREATE);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etxMessage.getText() != null) {
                    String message = TopicName + "#-#" + etxMessage.getText().toString();
                    sendMessage(RecipientId, message);
                    etxMessage.setText("");
                    hideSoftKeyboard(ChatMessageActivity.this);

                }
            }
        });
    }

    //This function set the view and members of this activity
    public void setChat(ChatItem chat) {
        RecipientId = chat.getPersonId();
        TopicName = chat.getTopic();
        txtPerson.setText(chat.getPerson());
        txtTopic.setText(chat.getTopic());

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", RecipientId);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(final List<ParseUser> list, ParseException e) {
                if (e == null) {
                    if (!list.isEmpty()) {
                        ParseUser RecipientUser = list.get(0);
                        if (RecipientUser != null) {
                            ParseFile profilePicture = (ParseFile) RecipientUser.get(ParseAdapter.KEY_PICTURE);
                            if(profilePicture != null) {
                                profilePicture.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] bytes, ParseException e) {
                                        if (e == null) {
                                            if (bytes != null) {
                                                Bitmap picture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                RoundImage roundImage = new RoundImage(picture);
                                                imgPerson.setImageDrawable(roundImage);
                                            }
                                        }
                                    }
                                });
                            }else {
                                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.default_profile);
                                roundImage = new RoundImage(image);
                                imgPerson.setImageDrawable(roundImage);
                            }
                        }
                    }
                }
            }
        });

        ParseQuery<ParseObject> topicQuery = ParseQuery.getQuery("ParseChat");
        topicQuery.whereEqualTo("objectId", chat.getTopicId());
        topicQuery.include("parseMessage");
        topicQuery.setLimit(5);
        topicQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (!list.isEmpty()) {
                    parseChat = list.get(0);
                    populateMessageHistory(parseChat);
                } else {
                    Log.d("TEST", "FAILED");
                }
            }
        });
    }

    //calls the async task populateMessages
    public void populateMessageHistory(ParseObject topic) {

        new populateMessages().execute(topic);
    }

    //async call that fills the Listview with messages
    private class populateMessages extends AsyncTask<ParseObject, MessageWrapper, Void> {

        @Override
        protected Void doInBackground(ParseObject... topic) {
            if (topic[0] != null) {
                ArrayList<ParseObject> topics = (ArrayList<ParseObject>) topic[0].get("parseMessage");
                if (topics != null) {
                    for (int i = 0; i < topics.size(); i++) {
                        try {
                            ParseObject object = topics.get(i).fetch();
                            WritableMessage message = new WritableMessage(object.get("recipientId").toString(), object.get("messageText").toString());
                            if (object.get("senderId").toString().equals(UserId)) {
                                publishProgress(new MessageWrapper(message, 1));
                            } else {
                                publishProgress(new MessageWrapper(message, 0));
                            }
                        } catch (com.parse.ParseException ex) {
                            Log.d("ERROR", "Failed " + ex.getLocalizedMessage());
                        }
                    }
                } else {
                    Log.d("TEST", "no topics");
                }
            } else {
                Log.d("TEST", "no topic");
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(MessageWrapper... i) {
            messageAdapter.addMessage(i[0].writableMessage, i[0].direction);
        }

        @Override
        protected void onPostExecute(Void v){

        }
    }

    //sends message via Sinch
    public void sendMessage(String recipient, String message) {
        if(!message.isEmpty()){
            messageServiceInterface.sendMessage(recipient, message);
        }
    }

    @Override
    public void onDestroy() {
        messageServiceInterface.removeMessageClientListener(messageClientListener);
        unbindService(serviceConnection);
        super.onDestroy();
    }

    //async task that adds a received message to the database
    private class addMessageToDataBase extends AsyncTask<AddMessageWrapper, Void, Void> {

        @Override
        protected Void doInBackground(AddMessageWrapper... myMessage) {
            final ArrayList<ParseObject> topics = new ArrayList<>();
            AddMessageWrapper wrapper = myMessage[0];
                    if(!wrapper.list.isEmpty())
                        for(int i = 0; i < wrapper.list.size(); i++){
                            if(wrapper.list.get(i).get("senderId").equals(UserId) || wrapper.list.get(i).get("recipientId").equals(UserId)){
                                final ParseObject currentTopic = wrapper.list.get(i);
                                ArrayList<ParseObject> topicList = (ArrayList<ParseObject>) wrapper.list.get(i).get("parseMessage");
                                if (topicList == null) {
                                    final ParseObject parseMessage = new ParseObject("ParseMessage");
                                    parseMessage.put("senderId", wrapper.message.getSenderId());
                                    parseMessage.put("recipientId", wrapper.writableMessage.getRecipientIds().get(0));
                                    parseMessage.put("messageText", wrapper.writableMessage.getTextBody());
                                    parseMessage.put("sinchId", wrapper.message.getMessageId());
                                    parseMessage.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {

                                            topics.add(parseMessage);
                                            currentTopic.put("parseMessage", topics);
                                            currentTopic.saveInBackground();
                                        }
                                    });
                                } else {
                                    for (int j = 0; j < topicList.size(); j++) {
                                        try {
                                            ParseObject object = topicList.get(j).fetch();
                                            if (object.get("sinchId").equals(wrapper.message.getMessageId())) {
                                                messageTrue = true;
                                            }else{
                                                topics.add(object);
                                            }
                                        } catch (com.parse.ParseException ex) {
                                            Log.d("ERROR", "onMessageReceive " + ex.getLocalizedMessage());
                                        }
                                    }
                                    if (!messageTrue) {
                                        Log.d("TEST", "onMessageExist1");
                                        final ParseObject parseMessageMore = new ParseObject("ParseMessage");
                                        parseMessageMore.put("senderId", wrapper.message.getSenderId());
                                        parseMessageMore.put("recipientId", wrapper.writableMessage.getRecipientIds().get(0));
                                        parseMessageMore.put("messageText", wrapper.writableMessage.getTextBody());
                                        parseMessageMore.put("sinchId", wrapper.message.getMessageId());
                                        parseMessageMore.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {

                                                topics.add(parseMessageMore);
                                                currentTopic.put("parseMessage", topics);
                                                currentTopic.saveInBackground();
                                                messageTrue = false;
                                            }
                                        });
                                    }
                                }
                            }
                        }
            return null;
        }
        @Override
        protected void onPostExecute(Void v){

        }
    }

    //From http://stackoverflow.com/questions/4165414/how-to-hide-soft-keyboard-on-android-after-clicking-outside-edittext
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    //wrapper for populateMessages async call
    public class MessageWrapper{
        public final WritableMessage writableMessage;
        public final Integer direction;

        public MessageWrapper(WritableMessage myMessage, Integer myInteger){
            writableMessage = myMessage;
            direction = myInteger;
        }
    }
    //wrapper for the addMessageToDatabase async call
    public class AddMessageWrapper{
        public final Message message;
        public final WritableMessage writableMessage;
        public final List<ParseObject> list;

        AddMessageWrapper(Message m, WritableMessage w, List<ParseObject> l){
            message = m;
            writableMessage = w;
            list = l;
        }
    }

//implements the ServiceConnection for the bind
    private class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messageServiceInterface = (MessageService.MessageServiceInterface) iBinder;
            messageServiceInterface.addMessageClientListener(messageClientListener);
            Log.d("Service connection", "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            messageServiceInterface = null;
        }
    }

    //Class that implement MessageClientListener for the Sinch Service
    private class MyMessageClientListener implements MessageClientListener {
        @Override
        public void onMessageFailed(MessageClient client, Message message, MessageFailureInfo failureInfo) {
            Toast.makeText(ChatMessageActivity.this, "Message Failed" + failureInfo, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onIncomingMessage(MessageClient client, final Message message) {
            Toast.makeText(ChatMessageActivity.this, "message", Toast.LENGTH_LONG).show();

            //Log.d("Test", client.toString());
            Log.d("Test", "Sender: " + message.getSenderId());
            Log.d("Test", "Recipient: " + message.getRecipientIds().get(0));

            String[] parts = message.getTextBody().split("#-#");
            String part1 = parts[0];

            final WritableMessage writableMessage = new WritableMessage(message.getRecipientIds().get(0), message.getTextBody());

            if (message.getSenderId().equals(chat.getPersonId())
                    && part1.equals(chat.getTopic())) {
                Log.d("Test", "succes");
                messageAdapter.addMessage(writableMessage, MessageAdapter.DIRECTION_INCOMING);
            }

            ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseChat");
            query.whereEqualTo("topic", part1);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    new addMessageToDataBase().execute(new AddMessageWrapper(message, writableMessage, list));
                }
            });
        }

        @Override
        public void onMessageSent(MessageClient client, Message message, String recipientId) {

            final WritableMessage writableMessage = new WritableMessage(message.getRecipientIds().get(0), message.getTextBody());
            messageAdapter.addMessage(writableMessage, MessageAdapter.DIRECTION_OUTGOING);

            String userIds[] = {chat.getPersonId(), UserId};
            ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseChat");
            query.whereEqualTo("topic", chat.getTopic());
            query.whereContainedIn("senderId", Arrays.asList(userIds));
            query.whereContainedIn("recipientId", Arrays.asList(userIds));
            //query.whereEqualTo("senderId", UserId);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> messageList, com.parse.ParseException e) {
                    if (e == null) {
                        if (messageList.size() == 0) {
                            ParseObject parseChat = new ParseObject("ParseChat");
                            parseChat.put("senderId", UserId);
                            parseChat.put("recipientId", chat.getPersonId());
                            parseChat.put("topic", chat.getTopic());
                            parseChat.saveInBackground();
                        }
                    }
                }
            });
        }

        @Override
        public void onMessageDelivered (MessageClient client, MessageDeliveryInfo deliveryInfo){
        }

        @Override
        public void onShouldSendPushData (MessageClient client, Message
                message, List <PushPair> pushPairs){
        }
    }
}
