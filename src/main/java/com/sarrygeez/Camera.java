package com.sarrygeez;

public class Camera extends Transform {

    private float zoom = 1;
    public float panSpeed = 50;
    public static Vector2 MOUSE_CAM_POS = new Vector2();

    public Camera() {

    }

    public void updateSize() {
        scale.x = translateSizeToScale(Application.WINDOW_WIDTH);
        scale.y = translateSizeToScale(Application.WINDOW_HEIGHT);
        updateBbox();
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public static float getMouseX() {
        return MOUSE_CAM_POS.x;
    }

    public static float getMouseY() {
        return MOUSE_CAM_POS.y;
    }

    public static int getMouseX_int() {
        return (int) MOUSE_CAM_POS.x;
    }

    public static int getMouseY_int() {
        return (int) MOUSE_CAM_POS.y;
    }
}
