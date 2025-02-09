package com.sarrygeez.Tools;

import com.sarrygeez.Data.Transform;
import com.sarrygeez.Data.Vector2;

import java.awt.*;
import java.awt.geom.AffineTransform;

@SuppressWarnings("unused")
public class AppGraphics {

    public enum GraphicMode {
        CAMERA, GUI
    }

    private static GraphicMode mode = GraphicMode.CAMERA;
    private static Graphics2D gContext = null; // Graphical Context

    public static final Vector2 zoomPoint = new Vector2();
    public static float zoomValue = 0;

    private AppGraphics() {}
    // NOTE: an option to draw relative to the camera or the window
    //       DrawGUI - elements not affected by camera position or scale
    //       Draw    - elements affected by camera position or scale


    public static void setGContext(Graphics2D gContext) {
        AppGraphics.gContext = gContext;
    }

    // Set the mode to GUI (fixed to the screen)
    public static void useGUI() {
        mode = GraphicMode.GUI;
        applyTransform(null);
    }

    // Set the mode to CAMERA (affected by camera position)
    public static void useCamera(Transform camera) {
        mode = GraphicMode.CAMERA;
        applyTransform(camera);
    }

    public static GraphicMode getMode() {
        return mode;
    }

    // ====================================== PRIVATE FUNCTIONS ===========================

    private static void applyTransform(Transform camera) {
        gContext.setTransform(new AffineTransform());
        if (mode == GraphicMode.CAMERA) {
            gContext.translate(-camera.position.x, -camera.position.y);
            applyZoom(zoomValue);
        }

    }

    private static void applyZoom(float zoom) {
        gContext.scale(zoom, zoom);
    }

    // ====================================== DRAW FUNCTIONS =============================

    public static void drawRect(Transform transform, int radius,
            int borderThickness, Color bodyColor, Color borderColor) {

        // Border
        gContext.setColor(borderColor);
        gContext.fillRoundRect(
                (int) transform.position.x, (int) transform.position.y,
                (int) (transform.scaleToSize(true) + (borderThickness * 2)),
                (int) (transform.scaleToSize(false) + (borderThickness * 2)),
                radius, radius
        );

        // Body
        gContext.setColor(bodyColor);
        gContext.fillRoundRect(
                (int) (transform.position.x + borderThickness),
                (int) (transform.position.y + borderThickness),
                (int) transform.scaleToSize(true),
                (int) transform.scaleToSize(false),
                radius, radius
        );
    }

    public static void drawRect(Vector2 start, Vector2 end, int radius,
            int thickness, boolean fillBody, Color bodyColor, Color borderColor) {

        Vector2 dimension = MathUtils.abs(MathUtils.getVector(start, end));
        Vector2 anchor = MathUtils.min(start, end);
        if (fillBody) {
            gContext.setColor(bodyColor);
            gContext.fillRoundRect(
                    anchor.getX_int(), anchor.getY_int(),
                    dimension.getX_int(), dimension.getY_int(),
                    radius, radius
            );
        }

        // Border
        gContext.setColor(borderColor);
        gContext.setStroke(new BasicStroke(thickness));
        gContext.drawRoundRect(
                anchor.getX_int(), anchor.getY_int(),
                dimension.getX_int(), dimension.getY_int(),
                radius, radius
        );
    }


    public static void drawPoint(Vector2 position,
            int thickness, boolean isOutline, Color color) {

        gContext.setColor(color);
        int offset = thickness / 2;

        if (isOutline) {
            gContext.drawOval((int)position.x - offset,
                    (int)position.y - offset,
                    thickness, thickness);
        } else {
            gContext.fillOval((int)position.x - offset,
                    (int)position.y - offset,
                    thickness, thickness);
        }

    }



    public static void drawLine(Vector2 start, Vector2 end, int thickness, Color color) {
        gContext.setColor(color);
        gContext.setStroke(new BasicStroke(thickness));
        gContext.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
        gContext.setStroke(new BasicStroke());
    }

/*
    text-alignment diagram: (for Vertical alignment)
    Top:
        ■──────────────┐
        │ Sample Text  │
        └──────────────┘

    Bottom:
        ┌──────────────┐
        │ Sample Text  │
        ■──────────────┘

    Center:
        ┌──────────────┐
        ■ Sample Text  │
        └──────────────┘
*/

    /**
     * HorizontalAlignment:<br>
     *      1 = Leading<br>
     *      2 = Trailing<br>
     *      3 = center
     *      <br><br>
     * VerticalAlignment:<br>
     *      1 = top<br>
     *      2 = bottom<br>
     *      3 = center
     */
    public static void drawText(Vector2 position, String text) {
        drawTextExt(position, text, gContext.getColor(), 1, 2);
        // This follows the same as the gContext.drawString();
    }


    /**
     * HorizontalAlignment:<br>
     *      1 = Leading<br>
     *      2 = Trailing<br>
     *      3 = center
     *      <br><br>
     * VerticalAlignment:<br>
     *      1 = top<br>
     *      2 = bottom<br>
     *      3 = center
     */
    public static void drawTextExt(Vector2 position,
            String text, Color color, int h_alignment, int v_alignment) {

        gContext.setColor(color);
        FontMetrics fm = gContext.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int offsetX = position.getX_int();
        switch (h_alignment) {
            case 2:
                offsetX -=  textWidth;
                break;
            case 3:
                offsetX -= textWidth/2;
                break;
        }

        int fontHeight = fm.getHeight();
        int offsetY = position.getY_int()+fontHeight;
        switch (v_alignment){
            case 2:
                offsetY -= fontHeight;
                break;
            case 3:
                offsetY -= (int) (fontHeight*0.75f); // IDK anymore, fuck this arbitrary number bro
                break;
        }

        String[] lines = text.split("\n");
        for(String line : lines) {
            gContext.drawString(line, offsetX, offsetY);
            offsetY += fontHeight;
        }
    }
}