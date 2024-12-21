package com.sarrygeez;

import java.awt.*;

public class RectComp {

    public Transform transform;
    public int radius = 0;
    public int borderWidth = 1;
    public Color background;
    public Color borderColor;

    public RectComp(int x, int y, Color background, Color borderColor) {
        this.transform = new Transform(x, y);
        this.background = background;
        this.borderColor = borderColor;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }
}
