package com.sarrygeez;

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

    public void updateCameraMousePosition(Vector2 basePos) {
        Camera.MOUSE_CAM_POS.x = basePos.getX_int() + position.x;
        Camera.MOUSE_CAM_POS.y = basePos.getY_int() + position.y;
    }

    /**
     * ONLY USED FOR TEXTS
     * @return the converted position
     */
    public Vector2 getCartesianPosition() {
        return new Vector2(
            position.getX_int() + Application.WINDOW_WIDTH/2,
            position.getY_int() + Application.WINDOW_HEIGHT/2
        );
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
