package com.jacobmosehansen.themeproject.Chat;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jacobmosehansen.themeproject.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
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

public class ChatActivity extends AppCompatActivity implements ChatInterface {

    private ChatListFragment chatListFragment;
    private ChatWindowFragment chatWindowFragment;
    private ArrayList<ChatItem> chatItems;
    private FrameLayout ChatListContainer;
    private FrameLayout ChatWindowContainer;
    private String UserId;
    private String RecipientId;
    private boolean exists = false;
    private MessageAdapter messageAdapter;
    private MessageService.MessageServiceInterface messageServiceInterface;
    private ServiceConnection serviceConnection = new MyServiceConnection();
    private MessageClientListener messageClientListener = new MyMessageClientListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        UserId = ParseUser.getCurrentUser().getObjectId();

        ChatListContainer = (FrameLayout) findViewById(R.id.chat_list_container);
        ChatWindowContainer = (FrameLayout) findViewById(R.id.chat_window_container);

        chatListFragment = new ChatListFragment();
        chatWindowFragment = new ChatWindowFragment();

        bindService(new Intent(this, MessageService.class), serviceConnection, BIND_AUTO_CREATE);

        chatItems = new ArrayList<>();

        chatItems.add(new ChatItem("Topic1", "Person1", "hbpZsfv8bf"));

        populateMessageList();

        Intent intent = getIntent();
        RecipientId = intent.getStringExtra("RECIPIENT_ID");

        getFragmentManager().beginTransaction()
                .add(R.id.chat_list_container, chatListFragment, "CHAT_LIST")
                .add(R.id.chat_window_container, chatWindowFragment, "CHAT_WINDOW")
                .commit();

        if(RecipientId != null){
            setVisible(ChatWindowContainer);
            chatWindowFragment.setChat(RecipientId);
        }else{
            setVisible(ChatListContainer);
        }


    }

    public void onChatSelected(int pos) {
        chatWindowFragment.setChat(RecipientId);
        setVisible(ChatWindowContainer);
    }

    public void setVisible(FrameLayout frame){
        if(frame == ChatListContainer) {
            ChatListContainer.setVisibility(View.VISIBLE);
            ChatWindowContainer.setVisibility(View.GONE);
        }else{
            ChatListContainer.setVisibility(View.GONE);
            ChatWindowContainer.setVisibility(View.VISIBLE);
        }
    }

    public ArrayList<ChatItem> getChatList(){
        return chatItems;
    }

    public void populateMessageHistory(String recipient) {
        String[] userIds = {UserId, recipient};
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseMessage");
        query.whereContainedIn("senderId", Arrays.asList(userIds));
        query.whereContainedIn("recipientId", Arrays.asList(userIds));
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messageList, com.parse.ParseException e) {
                if (e == null) {
                    for (int i = 0; i < messageList.size(); i++) {
                        WritableMessage message = new WritableMessage(messageList.get(i).get("recipientId").toString(), messageList.get(i).get("messageText").toString());
                        if (messageList.get(i).get("senderId").toString().equals(UserId)) {
                            chatWindowFragment.addMessageToList(message, MessageAdapter.DIRECTION_OUTGOING);
                        } else {
                            chatWindowFragment.addMessageToList(message, MessageAdapter.DIRECTION_INCOMING);
                        }
                    }
                }
            }
        });
    }

    public void populateMessageList(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseMessage");
        query.whereEqualTo("senderId", UserId);
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null)
                    for(int i=0; i < list.size(); i++){
                        String recipient = list.get(i).get("recipientId").toString();
                        if(!chatItems.isEmpty()){
                            for(int j = 0; i < chatItems.size(); i++){
                                if(recipient == chatItems.get(i).getPerson()){
                                    exists = true;
                                }
                            }
                        }
                        if(!exists){
                            chatItems.add(new ChatItem("Topic", "recipient"));
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
        public void onMessageFailed(MessageClient client, Message message, MessageFailureInfo failureInfo) {}

        @Override
        public void onIncomingMessage(MessageClient client, final Message message) {
            Toast.makeText(ChatActivity.this, "message", Toast.LENGTH_LONG).show();
            if (message.getSenderId().equals(chatWindowFragment.getRecipientId())) {
                final WritableMessage writableMessage = new WritableMessage(message.getRecipientIds().get(0), message.getTextBody());

                //only add message to parse database if it doesn't already exist there
                ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseMessage");
                query.whereEqualTo("sinchId", message.getMessageId());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> messageList, com.parse.ParseException e) {
                        if (e == null) {
                            if (messageList.size() == 0) {
                                ParseObject parseMessage = new ParseObject("ParseMessage");
                                parseMessage.put("senderId", UserId);
                                parseMessage.put("recipientId", writableMessage.getRecipientIds().get(0));
                                parseMessage.put("messageText", writableMessage.getTextBody());
                                parseMessage.put("sinchId", message.getMessageId());
                                parseMessage.saveInBackground();

                                chatWindowFragment.addMessageToList(writableMessage, MessageAdapter.DIRECTION_INCOMING);
                            }
                        }
                    }
                });
            }
        }

        @Override
        public void onMessageSent(MessageClient client, Message message, String recipientId) {

            final WritableMessage writableMessage = new WritableMessage(message.getRecipientIds().get(0), message.getTextBody());
            chatWindowFragment.addMessageToList(writableMessage, MessageAdapter.DIRECTION_OUTGOING);
        }

        @Override
        public void onMessageDelivered(MessageClient client, MessageDeliveryInfo deliveryInfo) {}

        @Override
        public void onShouldSendPushData(MessageClient client, Message message, List<PushPair> pushPairs) {}
    }
}
