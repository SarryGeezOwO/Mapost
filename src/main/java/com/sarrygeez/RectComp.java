package com.sarrygeez;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RectComp {

    public Transform transform;
    public int radius = 0;
    public int borderWidth = 1;
    public Color background;
    public Color borderColor;
    public static final int UPDATE_TIME = 50;
    private boolean mouseInside = false;

    private final List<MouseActionAdapter> adapters = new ArrayList<>();

    public RectComp(int x, int y, Color background, Color borderColor) {
        this.transform = new Transform(x, y);
        this.background = background;
        this.borderColor = borderColor;

        new Thread(() -> {

            try {
                while(true) {

                    // mouse events
                    if(positionInsideBbox(Camera.MOUSE_CAM_POS)) {
                        if(!mouseInside) {
                            this.borderColor = new Color(0, 0, 255);
                            mouseInside = true;
                        }

                        for (MouseActionAdapter adapter : adapters) {
                            adapter.onMouseStay();
                        }
                    }
                    else {
                        if(mouseInside) {
                            this.borderColor = new Color(0, 0, 0);
                            mouseInside = false;
                        }
                    }

                    Thread.sleep(UPDATE_TIME);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void addMouseActionListener(MouseActionAdapter adapter) {
        adapters.add(adapter);
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
