package dk.iha.itsmap.e15.grp03.studybuddy.Post;

import android.graphics.drawable.Drawable;

public class Post {

    private Drawable _postImg;
    private String _title;
    private String _level;
    private String _subject;
    private String _post;
    private String _name;


    public Post(String title, String level, String subject, String postText, String name){

        this._title = title;
        this._level = level;
        this._subject = subject;
        this._post = postText;
        this._name = name;
    }


    public Post(String title, String level, String subject, String name, Drawable img){
        this._postImg = img;
        this._title = title;
        this._level = level;
        this._subject = subject;
        this._post="";
        this._name = name;
    }

    public Post(Drawable img, String title, String name, String subject, String level) {
        this._postImg = img;
        this._title = title;
        this._name = name;
        this._subject = subject;
        this._level = level;
    }

    public Drawable getPostImg() { return _postImg; }

    public void setPostImg(Drawable postImg) { _postImg = postImg; }

    public String getTitle() { return _title; }

    public void setTitle(String title) { this._title = title; }

    public String getLevel() { return _level; }

    public void setLevel(String level) { this._level = level; }

    public String getSubject() { return _subject; }

    public void setSubject(String subject) { this._subject = subject; }

    public String getPost() {
        return _post;
    }

    public void setPost(String post) {
        this._post = post;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }
}
