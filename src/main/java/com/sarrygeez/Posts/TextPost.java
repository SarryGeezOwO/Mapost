package com.sarrygeez.Posts;

import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.sarrygeez.AppGraphics;
import com.sarrygeez.Camera;
import com.sarrygeez.Vector2;

import java.awt.*;

@SuppressWarnings("unused")
public class TextPost extends Post{

    private String message;

    public TextPost(String author, String title, String message, String dateCreated) {
        super(author, title, dateCreated);
        this.message = message;
        radius = 10;
        transform.setScale(new Vector2(10, 5));
    }

    @Override
    public void draw(Graphics2D g2d, Camera camera) {
        super.draw(g2d, camera);
        Vector2 pos = transform.position;
        Vector2 size = new Vector2(transform.scaleToSize(true), transform.scaleToSize(false));
        AppGraphics.drawLine(
                g2d,
                camera,
                new Vector2(pos.x,            pos.y+30),
                new Vector2(pos.x + size.x,   pos.y+30),
                1,
                borderColor
        );

        Vector2 authPos = new Vector2(pos.x + 5, pos.y + 2);
        g2d.setFont(new Font(FlatInterFont.FAMILY, Font.BOLD, 16));
        AppGraphics.drawText(g2d, camera, authPos, getAuthor(), Color.BLACK, 1, 1);
        g2d.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 14));

        Vector2 msgPos = new Vector2(pos.x + 5, pos.y + 32);
        AppGraphics.drawText(g2d, camera, msgPos, getMessage(), Color.DARK_GRAY, 1, 1);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
