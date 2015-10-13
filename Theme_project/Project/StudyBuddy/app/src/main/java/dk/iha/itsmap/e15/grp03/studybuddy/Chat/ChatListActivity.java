package dk.iha.itsmap.e15.grp03.studybuddy.Chat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import dk.iha.itsmap.e15.grp03.studybuddy.R;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import dk.iha.itsmap.e15.grp03.studybuddy.Tools.ParseAdapter;
import dk.iha.itsmap.e15.grp03.studybuddy.Tools.RoundImage;

public class ChatListActivity extends AppCompatActivity {

    private ListView lvwChatList;
    private ChatListAdaptor chatListAdaptor;
    private ParseUser currentUser;
    private String UserId;
    private String ListRecipientId;
    private String ListTopic;
    private String ListTopicId;
    private String ListName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        currentUser = ParseUser.getCurrentUser();

        UserId = currentUser.getObjectId();

        lvwChatList = (ListView) findViewById(R.id.chat_List);

        chatListAdaptor = new ChatListAdaptor(ChatListActivity.this);

        populateMessageList();

        lvwChatList.setAdapter(chatListAdaptor);


        lvwChatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onChatSelected(position);
            }
        });
    }

    //This function fecthes the Chat Object from the database and puts them in the ListView
    public void populateMessageList() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseChat");
        query.orderByAscending("updatedAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                Boolean exists = false;
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getString("senderId").equals(UserId) || list.get(i).getString("recipientId").equals(UserId)) {
                            if (list.get(i).getString("senderId").equals(UserId)) {
                                ListRecipientId = list.get(i).get("recipientId").toString();
                            } else {
                                ListRecipientId = list.get(i).get("senderId").toString();
                            }
                            ListTopic = list.get(i).get("topic").toString();
                            ListTopicId = list.get(i).getObjectId();
                            if (!chatListAdaptor.getChatItems().isEmpty()) {
                                for (int j = 0; i < chatListAdaptor.getChatItems().size(); i++) {
                                    if (ListRecipientId.equals(chatListAdaptor.getChatItems().get(i).getPersonId())
                                            && ListTopic.equals(chatListAdaptor.getChatItems().get(i).getTopic())) {
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
                                                    if(profilePicture != null) {
                                                        profilePicture.getDataInBackground(new GetDataCallback() {
                                                           @Override
                                                           public void done(byte[] bytes, ParseException e) {
                                                               if (e == null) {
                                                                   if (bytes != null) {
                                                                       Bitmap picture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                                       RoundImage roundImage = new RoundImage(picture);
                                                                       chatListAdaptor.addMessage(new ChatItem(ListName, ListTopic, roundImage, ListRecipientId, ListTopicId));
                                                                   }
                                                               }
                                                           }
                                                        });
                                                    }else {
                                                        chatListAdaptor.addMessage(new ChatItem(ListName, ListTopic, ListRecipientId, ListTopicId));
                                                    }
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

    //This Function is called on the click of an item in the ListView and starts the ChatMessageActivity
    public void onChatSelected(int pos) {
        ChatItem chat = chatListAdaptor.getItem(pos);
        Intent intent = new Intent(ChatListActivity.this, ChatMessageActivity.class);
        intent.putExtra("CHATITEM", new ChatItem(chat.getPerson(), chat.getTopic(), chat.getPersonId(), chat.getTopicId()));
        startActivity(intent);
    }

}
