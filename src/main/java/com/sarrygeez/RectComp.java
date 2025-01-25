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

    public RectComp() {
        transform = new Transform(0, 0);
        background = Color.WHITE;
        borderColor = Color.BLACK;
        initThread();
    }

    public RectComp(int x, int y, Color background, Color borderColor) {
        this.transform = new Transform(x, y);
        this.background = background;
        this.borderColor = borderColor;
        initThread();
    }

    private void initThread() {
        new Thread(() -> {

            try {
                while(true) {

                    // mouse events
                    if(positionInsideBbox(Camera.MOUSE_CAM_POS)) {
                        if(!mouseInside) {
                            this.borderColor = Color.decode("#7235FF");
                            this.background = Color.decode("#D7BFFF");
                            mouseInside = true;
                        }

                        for (MouseActionAdapter adapter : adapters) {
                            adapter.onMouseStay();
                        }
                    }
                    else {
                        if(mouseInside) {
                            this.borderColor = new Color(0, 0, 0);
                            this.background = new Color(255, 255, 255);
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

    public void draw(Graphics2D g2d, Camera camera) {
        // override this
        AppGraphics.drawRect(g2d, camera, transform, radius, borderWidth, background, borderColor);
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
