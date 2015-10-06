package com.jacobmosehansen.themeproject.Chat;

import java.util.ArrayList;

/**
 * Created by Andersen on 06-10-2015.
 */
public interface ChatListInterface {

    public void onChatSelected(int pos);
    public ArrayList<ChatItem> getChatList();

}
