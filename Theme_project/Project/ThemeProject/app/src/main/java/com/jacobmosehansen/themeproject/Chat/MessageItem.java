package com.jacobmosehansen.themeproject.Chat;

/**
 * Created by Andersen on 06-10-2015.
 */
public class MessageItem {

    private String From;
    private String Message;

    MessageItem(String from, String message){
        From = from;
        Message = message;
    }

    public String getFrom(){
        return From;
    }

    public String getMessage() {
        return Message;
    }

    public void setFrom(String from) {
        From = from;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
