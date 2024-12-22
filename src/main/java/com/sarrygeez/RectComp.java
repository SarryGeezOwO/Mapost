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

    /**
     * Checks if the given position intersects with the bounding box of the
     * calling rectComp object
     * @param position The position to check, position should be based of the world space
     * @return a boolean whether the position is inside the bounding box
     */
    public boolean positionInsideBbox(Vector2 position) {
        return (
            position.x > transform.bbox_left &&
            position.x < transform.bbox_right &&
            position.y > transform.bbox_top &&
            position.y < transform.bbox_bottom
        );
    }
}
