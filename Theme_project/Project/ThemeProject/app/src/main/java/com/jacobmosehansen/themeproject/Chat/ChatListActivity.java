package com.jacobmosehansen.themeproject.Chat;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.jacobmosehansen.themeproject.R;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
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

public class ChatListActivity extends AppCompatActivity implements ChatListInterface {

    ChatListFragment chatListFragment;
    ChatWindowFragment chatWindowFragment;

    ArrayList<ChatItem> chatItems;

    FrameLayout ChatListContainer;
    FrameLayout ChatWindowContainer;

    String User;
    String recipient;

    MessageAdapter messageAdapter;
    private MessageService.MessageServiceInterface messageServiceInterface;
    private ServiceConnection serviceConnection = new MyServiceConnection();
    private MessageClientListener messageClientListener = new MyMessageClientListener();

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

    //get previous messages from parse & display
    private void populateMessageHistory() {
        String[] userIds = {User, recipient};
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
                        if (messageList.get(i).get("senderId").toString().equals(User)) {
                            chatWindowFragment.addMessageToList(message, MessageAdapter.DIRECTION_OUTGOING);
                        } else {
                            chatWindowFragment.addMessageToList(message, MessageAdapter.DIRECTION_INCOMING);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void sendMessage(String message) {
        if(message != "") {
            messageServiceInterface.sendMessage(recipient, message);
            unbindService(serviceConnection);
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
            if (message.getSenderId().equals(recipient)) {
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
                                parseMessage.put("senderId", User);
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
