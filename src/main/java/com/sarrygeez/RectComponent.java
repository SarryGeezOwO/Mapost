package com.sarrygeez;

import com.sarrygeez.Core.Rendering.Camera;
import com.sarrygeez.Data.Transform;
import com.sarrygeez.Data.Vector2;
import com.sarrygeez.Core.Inputs.MouseActionAdapter;
import com.sarrygeez.Tools.AppGraphics;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class RectComponent {

    public Transform transform;
    public int radius = 0;
    public int borderWidth = 1;
    public Color background;
    public Color borderColor;
    private boolean mouseInside = false;

    private final List<MouseActionAdapter> adapters = new ArrayList<>();

    public RectComponent() {
        transform = new Transform(0, 0);
        background = Color.WHITE;
        borderColor = Color.BLACK;
    }

    public RectComponent(int x, int y, Color background, Color borderColor) {
        this.transform = new Transform(x, y);
        this.background = background;
        this.borderColor = borderColor;
    }

    public void update() {
        // mouse events
        if(positionInsideBbox(Camera.MOUSE_CAM_POS)) {
            if(!mouseInside) {
                this.borderColor = Color.decode("#7235FF");
                this.background = Color.decode("#D7BFFF");
                this.mouseInside = true;
            }

            for (MouseActionAdapter adapter : adapters) {
                adapter.onMouseStay();
            }
        }
        else {
            if(mouseInside) {
                this.borderColor = new Color(0, 0, 0);
                this.background = new Color(255, 255, 255);
                this.mouseInside = false;
            }
        }
    }

    public void draw(Graphics2D g2d, Camera camera) {
        // override this
        AppGraphics.drawRect(camera, transform, radius, borderWidth, background, borderColor);
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
