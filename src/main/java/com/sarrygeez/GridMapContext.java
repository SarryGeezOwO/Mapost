package com.sarrygeez;

import java.util.ArrayList;
import java.util.List;

public class GridMapContext {

    public static final int CELL_SIZE = 32; // to determine positions and shit
    public List<RectComp> objects = new ArrayList<>();
    public static Vector2 MOUSE_POSITION = new Vector2(); // Screen mouse position

    public GridMapContext() {

    }

    public static float getMouseX() {
        return MOUSE_POSITION.x;
    }

    public static float getMouseY() {
        return MOUSE_POSITION.y;
    }

    public static int getMouseX_int() {
        return (int) MOUSE_POSITION.x;
    }

    public static int getMouseY_int() {
        return (int) MOUSE_POSITION.y;
    }
}
