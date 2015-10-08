package com.jacobmosehansen.themeproject.Post.PostListUtil;

/**
 * Created by Marlene on 08-10-2015.
 */
public class Post {

    private String overskrift;
    private String niveau;
    private String fag;
    private String post;
    private String author;

    public Post(String postOverskrift, String niveau, String fag, String postText, String author){

        overskrift = postOverskrift;
        this.niveau = niveau;
        this.fag = fag;
        post = postText;
        this.author = author;
    }

    public String getOverskrift() {
        return overskrift;
    }

    public void setOverskrift(String overskrift) {
        this.overskrift = overskrift;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getFag() {
        return fag;
    }

    public void setFag(String fag) {
        this.fag = fag;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
