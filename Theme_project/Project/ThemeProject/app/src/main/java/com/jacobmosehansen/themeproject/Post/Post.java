package com.jacobmosehansen.themeproject.Post;

/**
 * Created by Marlene on 08-10-2015.
 */
public class Post {

    private String headline;
    private String level;
    private String course;
    private String post;
    private String name;

    public Post(String postHeadline, String level, String course, String postText, String name){

        headline = postHeadline;
        this.level = level;
        this.course = course;
        post = postText;
        this.name = name;
    }

    public Post(String postHeadline, String level, String course, String name){

        headline = postHeadline;
        this.level = level;
        this.course = course;
        post="";
        this.name = name;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String overskrift) {
        this.headline = overskrift;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getName() {
        return name;
    }

    public void setName(String author) {
        this.name = author;
    }
}
