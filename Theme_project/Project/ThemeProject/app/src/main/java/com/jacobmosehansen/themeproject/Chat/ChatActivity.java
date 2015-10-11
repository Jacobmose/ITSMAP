package com.jacobmosehansen.themeproject.Chat;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
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
import com.parse.SaveCallback;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;
import com.sinch.android.rtc.messaging.WritableMessage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements ChatInterface {

    private ChatListFragment chatListFragment;
    private ChatWindowFragment chatWindowFragment;
    private FrameLayout ChatListContainer;
    private FrameLayout ChatWindowContainer;
    private String UserId;
    private String RecipientId;
    private String Topic;
    private String ListRecipientId;
    private String ListTopic;
    private String ListName;
    private String ListTopicId;
    private boolean exists = false;
    private boolean LandscapeMode = false;
    private boolean ChatMode = false;
    private boolean messageTrue = false;
    private int RecipientListPosition;
    private ArrayList<ParseObject> topics;
    private MessageService.MessageServiceInterface messageServiceInterface;
    private ServiceConnection serviceConnection = new MyServiceConnection();
    private MessageClientListener messageClientListener = new MyMessageClientListener();
    private ParseObject currentTopic;
    private WritableMessage writableMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        UserId = ParseUser.getCurrentUser().getObjectId();

        ChatListContainer = (FrameLayout) findViewById(R.id.chat_list_container);
        ChatWindowContainer = (FrameLayout) findViewById(R.id.chat_window_container);

        bindService(new Intent(this, MessageService.class), serviceConnection, BIND_AUTO_CREATE);

        Intent intent = getIntent();
        RecipientId = intent.getStringExtra("RECIPIENT_ID");
        Topic = intent.getStringExtra("TOPIC_ID");

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            LandscapeMode = true;
            Log.d("orientation", "Landscape");
        }else{
            LandscapeMode = false;
            Log.d("orientation", "Portrait");
        }

        if(savedInstanceState == null) {

            RecipientListPosition = 0;

            chatListFragment = new ChatListFragment();
            chatWindowFragment = new ChatWindowFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.chat_list_container, chatListFragment, "CHAT_LIST")
                    .add(R.id.chat_window_container, chatWindowFragment, "CHAT_WINDOW")
                    .commit();

            /*if(!chatListFragment.getChatItems().isEmpty()){
                chatWindowFragment.setChat(chatListFragment.getChat(0));
            }*/

            setScreen();

        } else {
            RecipientListPosition = savedInstanceState.getInt("RECIPIENT_LIST_POSITION");
            ChatMode = savedInstanceState.getBoolean("CHAT_MODE");

            chatListFragment = (ChatListFragment) getSupportFragmentManager().findFragmentByTag("CHAT_LIST");
            if(chatListFragment==null){
                chatListFragment = new ChatListFragment();
            }
            chatWindowFragment = (ChatWindowFragment)getSupportFragmentManager().findFragmentByTag("CHAT_WINDOW");
            if(chatWindowFragment==null){
                chatWindowFragment = new ChatWindowFragment();
            }

            setScreen();
        }

        if(RecipientId != null) {
            ChatItem chat = new ChatItem(Topic, "Test", RecipientId, "memee");
            chatWindowFragment.setChat(chat);
            ChatMode = true;
            setScreen();
        }
    }

    public void setScreen(){
        if(!LandscapeMode){
            if(ChatMode){
                ChatListContainer.setVisibility(View.GONE);
                ChatWindowContainer.setVisibility(View.VISIBLE);
            }else{
                ChatWindowContainer.setVisibility(View.GONE);
                ChatListContainer.setVisibility(View.VISIBLE);
            }
        }else{
            ChatListContainer.setVisibility(View.VISIBLE);
            ChatWindowContainer.setVisibility(View.VISIBLE);
        }

    }

    public void onChatSelected(int pos) {
        chatWindowFragment.setChat(chatListFragment.getChat(pos));
        ChatMode = true;
        setScreen();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("RECIPIENT_LIST_POSITION", RecipientListPosition);
        outState.putBoolean("CHAT_MODE", ChatMode);
        super.onSaveInstanceState(outState);
    }

    public void populateMessageHistory(ParseObject topic) {
        if(topic != null){
            ArrayList<ParseObject> topics = (ArrayList<ParseObject>) topic.get("parseMessage");
            if(topics != null){
                for(int i = 0; i < topics.size(); i++ ){
                        try{
                            ParseObject object = topics.get(i).fetch();
                            WritableMessage message = new WritableMessage(object.get("recipientId").toString(), object.get("messageText").toString());
                            if (object.get("senderId").toString().equals(UserId)) {
                                chatWindowFragment.addMessageToList(message, MessageAdapter.DIRECTION_OUTGOING);
                            } else {
                                chatWindowFragment.addMessageToList(message, MessageAdapter.DIRECTION_INCOMING);
                            }
                        }catch (com.parse.ParseException ex){
                            Log.d("ERROR", "Failed " + ex.getLocalizedMessage());
                        }
                }
            }else {
                Log.d("TEST", "no topics");
            }
        }else{
            Log.d("TEST", "no topic");
        }

    }

    public void populateMessageList() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseChat");
        query.orderByAscending("updatedAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getString("senderId").equals(UserId) || list.get(i).getString("recipientId").equals(UserId)) {
                            if(list.get(i).getString("senderId").equals(UserId)){
                                ListRecipientId = list.get(i).get("recipientId").toString();
                            }else{
                                ListRecipientId = list.get(i).get("senderId").toString();
                            }
                            ListTopic = list.get(i).get("topic").toString();
                            ListTopicId = list.get(i).getObjectId();
                            if (!chatListFragment.getChatItems().isEmpty()) {
                                for (int j = 0; i < chatListFragment.getChatItems().size(); i++) {
                                    if (ListRecipientId.equals(chatListFragment.getChatItems().get(i).getPersonId())
                                            && ListTopic.equals(chatListFragment.getChatItems().get(i).getTopic())) {
                                        exists = true;
                                    }
                                }
                            }
                            if (!exists) {
                                ParseQuery<ParseUser> query = ParseUser.getQuery();
                                query.whereEqualTo("objectId", ListRecipientId);
                                query.findInBackground(new FindCallback<ParseUser>() {
                                    @Override
                                    public void done(final List<ParseUser> list, ParseException e) {
                                        if (e == null) {
                                            if (!list.isEmpty()) {
                                                ParseUser RecipientUser = list.get(0);
                                                if (RecipientUser != null) {
                                                    ListName = RecipientUser.getUsername();
                                                    ParseFile profilePicture = (ParseFile) RecipientUser.get(ParseAdapter.KEY_PICTURE);
                                                    profilePicture.getDataInBackground(new GetDataCallback() {
                                                        @Override
                                                        public void done(byte[] bytes, ParseException e) {
                                                            if (e == null) {
                                                                if (bytes != null) {
                                                                    Bitmap picture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                                    RoundImage roundImage = new RoundImage(picture);
                                                                    chatListFragment.addChatToList(new ChatItem(ListName, ListTopic, roundImage, ListRecipientId, ListTopicId));
                                                                } else {
                                                                    chatListFragment.addChatToList(new ChatItem(ListName, ListTopic, ListRecipientId, ListTopicId));
                                                                }
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });
    }


    public void sendMessage(String recipient, String message) {
        if(!message.isEmpty()){
            messageServiceInterface.sendMessage(recipient, message);
        }
    }

    @Override
    public void onDestroy() {
        messageServiceInterface.removeMessageClientListener(messageClientListener);
        super.onDestroy();
    }

    private class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messageServiceInterface = (MessageService.MessageServiceInterface) iBinder;
            messageServiceInterface.addMessageClientListener(messageClientListener);
            Log.d("Service connection", "onConnect");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            messageServiceInterface = null;
        }
    }

    private class MyMessageClientListener implements MessageClientListener {
        @Override
        public void onMessageFailed(MessageClient client, Message message, MessageFailureInfo failureInfo) {
            Toast.makeText(ChatActivity.this, "Message Failed" + failureInfo, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onIncomingMessage(MessageClient client, final Message message) {
            Log.d("Log", "onMessage1");
            Toast.makeText(ChatActivity.this, "message", Toast.LENGTH_LONG).show();

            String[] parts = message.getTextBody().split("#-#");
            String part1 = parts[0];
            String part2 = parts[1];

            Log.d("Log", "onMessage2");

            writableMessage = new WritableMessage(message.getRecipientIds().get(0), message.getTextBody());

            currentTopic = chatWindowFragment.getTopic();
            if(currentTopic != null){
                if (message.getSenderId().equals(chatWindowFragment.getRecipientId())
                        || part1.equals(currentTopic.get("topic").toString())) {
                    Log.d("Log", "onMessage3");
                    chatWindowFragment.addMessageToList(writableMessage, MessageAdapter.DIRECTION_INCOMING);
                }
            }

            Log.d("Log", "onMessage4");
            topics = new ArrayList<ParseObject>();

            ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseChat");
            query.whereEqualTo("topic", part1);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if(!list.isEmpty())
                        for(int i = 0; i < list.size(); i++){
                            if(list.get(i).get("senderId").equals(UserId) || list.get(i).get("recipientId").equals(UserId)){
                                currentTopic = list.get(i);
                                ArrayList<ParseObject> topicList = (ArrayList<ParseObject>) list.get(i).get("parseMessage");
                                if (topicList == null) {
                                    Log.d("Log", "onMessage5");
                                    final ParseObject parseMessage = new ParseObject("ParseMessage");
                                    parseMessage.put("senderId", UserId);
                                    parseMessage.put("recipientId", writableMessage.getRecipientIds().get(0));
                                    parseMessage.put("messageText", writableMessage.getTextBody());
                                    parseMessage.put("sinchId", message.getMessageId());
                                    parseMessage.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {

                                            topics.add(parseMessage);
                                            currentTopic.put("parseMessage", topics);
                                            currentTopic.saveInBackground();
                                        }
                                    });



                                } else {
                                    Log.d("TEST", "onMessageExist1");
                                        for (int j = 0; j < topicList.size(); j++) {
                                            Log.d("TEST", "onMessageExist2");
                                            try {
                                                Log.d("TEST", "onMessageExist3");
                                                ParseObject object = topicList.get(i).fetch();
                                                if (object.get("sinchId").equals(message.getMessageId())) {
                                                    Log.d("TEST", "onMessageExist4");
                                                    messageTrue = true;
                                                }else{
                                                    Log.d("TEST", "onMessageExist4");
                                                    //topics.add(object);
                                                }
                                            } catch (com.parse.ParseException ex) {
                                                Log.d("ERROR", "onMessageReceive " + ex.getLocalizedMessage());
                                            }
                                        }
                                    if (!messageTrue) {
                                        Log.d("TEST", "onMessageExist1");
                                        final ParseObject parseMessageMore = new ParseObject("ParseMessage");
                                        parseMessageMore.put("senderId", UserId);
                                        parseMessageMore.put("recipientId", writableMessage.getRecipientIds().get(0));
                                        parseMessageMore.put("messageText", writableMessage.getTextBody());
                                        parseMessageMore.put("sinchId", message.getMessageId());
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
                }
            });
    }

        @Override
        public void onMessageSent(MessageClient client, Message message, String recipientId) {

            final WritableMessage writableMessage = new WritableMessage(message.getRecipientIds().get(0), message.getTextBody());
            chatWindowFragment.addMessageToList(writableMessage, MessageAdapter.DIRECTION_OUTGOING);

            String userIds[] = {chatWindowFragment.getRecipientId(), UserId};
            ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseChat");
            query.whereEqualTo("topic", chatWindowFragment.getTopic());
            query.whereContainedIn("senderId", Arrays.asList(userIds));
            query.whereContainedIn("recipientId", Arrays.asList(userIds));
            query.whereEqualTo("senderId", UserId);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> messageList, com.parse.ParseException e) {
                    if (e == null) {
                        if (messageList.size() == 0) {
                            ParseObject parseChat = new ParseObject("ParseChat");
                            parseChat.put("senderId", UserId);
                            parseChat.put("recipientId", chatWindowFragment.getRecipientId());
                            parseChat.put("topic", chatWindowFragment.getTopic());
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
                message, List < PushPair > pushPairs){
        }
    }
}
