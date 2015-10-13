package com.jacobmosehansen.themeproject.Chat;


import android.graphics.drawable.Drawable;
import java.io.Serializable;

/**
 * Created by Andersen on 06-10-2015.
 */

//This is a wrapper class for the ChatItem for the ListView and the ChatMessageActivity
@SuppressWarnings("serial")
public class ChatItem implements Serializable {

    private String Person;
    private String Topic;
    private Drawable PersonImg;
    private String PersonId;
    private String TopicId;

    public ChatItem(String person, String topic, Drawable img, String personId, String topicId) {
        Person = person;
        Topic = topic;
        PersonImg = img;
        PersonId = personId;
        TopicId = topicId;
    }

    public ChatItem(String person, String topic, String personId, String topicId) {
        Person = person;
        Topic = topic;
        PersonId = personId;
        TopicId = topicId;
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

    public String getTopicId(){return TopicId;}
}

