package com.sarrygeez.Posts;

import com.sarrygeez.RectComp;

@SuppressWarnings("unused")
public abstract class Post extends RectComp {

    private String author;
    private String title;
    private String dateCreated;

    public Post(String author, String title, String dateCreated) {
        super();
        this.author = author;
        this.title = title;
        this.dateCreated = dateCreated;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
