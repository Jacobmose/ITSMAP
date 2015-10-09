package com.jacobmosehansen.themeproject.Chat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import com.jacobmosehansen.themeproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andersen on 06-10-2015.
 */
public class ChatItem {

    private String Person;
    private String Topic;
    private Drawable PersonImg;
    private String PersonId;

    public ChatItem(String person, String topic, Drawable img, String personId) {
        Person = person;
        Topic = topic;
        PersonImg = img;
        PersonId = personId;
    }

    public ChatItem(String person, String topic, String personId) {
        Person = person;
        Topic = topic;
        PersonId = personId;
    }

    public ChatItem(String person, String topic, Drawable img){
        Person = person;
        Topic = topic;
        PersonImg = img;
    }
    public ChatItem(String person, String topic){
        Person = person;
        Topic = topic;
        PersonImg = null;
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

    public String getPersonId(){return PersonId;}

    public void setPerson(String person) {
        Person = person;
    }

    public void setTopic(String topic) {
        Topic = topic;
    }

    public void setPersonImg(Drawable personImg) {
        PersonImg = personImg;
    }

    public void setPersonId(String personId){PersonId = personId;}




}

