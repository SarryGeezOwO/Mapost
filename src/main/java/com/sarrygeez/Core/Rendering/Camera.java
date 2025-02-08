package com.sarrygeez.Core.Rendering;

import com.sarrygeez.Application;
import com.sarrygeez.Data.Transform;
import com.sarrygeez.Data.Vector2;

public class Camera extends Transform {

    //NOTE: WHENEVER THE CAMERA POSITION IS GOING TO BE DISPLAYED AS A TEXT, ALWAYS TURN IT INTO A CARTESIAN BASED SYSTEM
    //      WHY? BECAUSE I SUCK AND FORGOT TO DO IT THAT WAY IN THE FIRST PLACE

    private float zoom = 1;
    public float panSpeed = 50;
    public static Vector2 MOUSE_CAM_POS = new Vector2();

    public Camera() {
        position = Application.toCartesianCoordinate(new Vector2(0, 0));
    }

    public void updateSize() {
        scale.x = translateSizeToScale(Application.WINDOW_WIDTH);
        scale.y = translateSizeToScale(Application.WINDOW_HEIGHT);
        updateBbox();
    }

    // used in zooms and shit IDK Anymore...
    public void updateCameraMousePosition(Vector2 basePos) {
        float adjustedX = (basePos.x + position.x) / zoom;
        float adjustedY = (basePos.y + position.y) / zoom;

        Camera.MOUSE_CAM_POS.x = adjustedX;
        Camera.MOUSE_CAM_POS.y = adjustedY;
    }

    /**
     * ONLY USED FOR TEXTS
     * @return the converted position
     */
    @Override
    public Vector2 getWorldPosition() {
        return new Vector2(
            (position.getX_int() + (float) Application.WINDOW_WIDTH /2) / (GridMapContext.CELL_SIZE * zoom),
            (position.getY_int() + (float) Application.WINDOW_HEIGHT /2) / (GridMapContext.CELL_SIZE * zoom)
        );
    }

    public Vector2 getWorldMousePosition() {
        return new Vector2(
                getMouseX() / GridMapContext.CELL_SIZE,
                getMouseY() / GridMapContext.CELL_SIZE);
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public float getZoom() {
        return zoom;
    }

    public static float getMouseX() {
        return MOUSE_CAM_POS.x;
    }

    public static float getMouseY() {
        return MOUSE_CAM_POS.y;
    }
}
