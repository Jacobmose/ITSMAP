package com.jacobmosehansen.themeproject.Chat;

import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by Andersen on 06-10-2015.
 */
public interface ChatInterface {

    public void onChatSelected(int pos);
    public void sendMessage(String recipient, String message);
    public void populateMessageHistory(ParseObject topic);
    public void populateMessageList();


}
