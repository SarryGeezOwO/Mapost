package com.sarrygeez.Posts;

import com.sarrygeez.Camera;

import java.awt.*;

public class TextPost extends Post{

    private String message;

    public TextPost(String author, String title, String dateCreated) {
        super(author, title, dateCreated);
    }

    @Override
    public void draw(Graphics2D g2d, Camera camera) {
        super.draw(g2d, camera);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
