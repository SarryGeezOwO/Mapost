package com.sarrygeez;

public class Camera extends Transform {

    private float zoom = 1;

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
}
