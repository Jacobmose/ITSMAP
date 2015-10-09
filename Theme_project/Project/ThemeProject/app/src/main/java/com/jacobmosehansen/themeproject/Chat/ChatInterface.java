package com.jacobmosehansen.themeproject.Chat;

import java.util.ArrayList;

/**
 * Created by Andersen on 06-10-2015.
 */
public interface ChatInterface {

    public void onChatSelected(int pos);
    public ArrayList<ChatItem> getChatList();
    public void sendMessage(String recipient, String message);
    public void populateMessageHistory(String recipient);
    public void populateMessageList();


}
