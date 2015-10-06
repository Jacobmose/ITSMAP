package com.jacobmosehansen.themeproject.Chat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import com.jacobmosehansen.themeproject.R;

import java.util.ArrayList;

/**
 * Created by Andersen on 06-10-2015.
 */
public class ChatItem {

    private String Person;
    private String Topic;
    private Drawable PersonImg;
    private ArrayList<MessageItem> MessageItems;

    public ChatItem(String person, String topic, Drawable img, ArrayList<MessageItem> messageItems) {
        Person = person;
        Topic = topic;
        PersonImg = img;
        MessageItems = messageItems;
    }

    public ChatItem(String person, String topic, Drawable img){
        Person = person;
        Topic = topic;
        PersonImg = img;
        MessageItems = null;
    }
    public ChatItem(String person, String topic){
        Person = person;
        Topic = topic;
        PersonImg = null;
        MessageItems = null;
    }

    public String getPerson(){
       return Person;
    }

    public String getTopic(){
        return Topic;
    }

    public Drawable getPersonImg() {
        return PersonImg;
    }

    public void setPerson(String person) {
        Person = person;
    }

    public void setTopic(String topic) {
        Topic = topic;
    }

    public void setPersonImg(Drawable personImg) {
        PersonImg = personImg;
    }

    public ArrayList<MessageItem> getMessageItems() {
        return MessageItems;
    }

    public void setMessageItems(ArrayList<MessageItem> messageItems) {
        MessageItems = messageItems;
    }

    public void addMessageItem(MessageItem messageItem){
        MessageItems.add(messageItem);
    }
}

