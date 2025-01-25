package com.sarrygeez;

@SuppressWarnings("unused")
public class Transform {

    public Vector2 position;
    public Vector2 scale;

    // Anchor is positioned in Top-left format
    public float bbox_left      = 0;
    public float bbox_right     = 0;
    public float bbox_top       = 0;
    public float bbox_bottom    = 0;

    public Transform() {
        position = new Vector2(0, 0);
        scale = new Vector2(1, 1);
        updateBbox();
    }

    public Transform(int x, int y) {
        position = new Vector2(x, y);
        scale = new Vector2(1, 1);
        updateBbox();
    }

    public Transform(float x, float y) {
        position = new Vector2(x, y);
        scale = new Vector2(1, 1);
        updateBbox();
    }

    public Transform(Vector2 pos) {
        position = pos;
        scale = new Vector2(1, 1);
        updateBbox();
    }

    public void updateBbox() {
        // Anchor is positioned in Top-left format
        bbox_left      = position.x;
        bbox_right     = position.x + scaleToSize(true);
        bbox_top       = position.y;
        bbox_bottom    = position.y + scaleToSize(false);
    }



    public void setX(int x) {
        position.x = x;
    }

    public void setY(int y) {
        position.y = y;
    }


    // Translates scale to a size value e.g,; Width and height
    public float scaleToSize(boolean isX) {
        return (int)(GridMapContext.CELL_SIZE * (isX ? scale.x : scale.y)); // Yes
    }

    public static float translateSizeToScale(float size) {
        if (size == 0) {
            return 0;
        }
        return size / GridMapContext.CELL_SIZE;
    }
}
