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

    private AppGraphics() {
    }
    // NOTE: an option to draw relative to the camera or the window
    //       DrawGUI - elements not affected by camera position or scale
    //       Draw    - elements affected by camera position or scale


    public static void setGContext(Graphics2D gContext) {
        AppGraphics.gContext = gContext;
    }

    // Set the mode to GUI (fixed to the screen)
    public static void useGUI() {
        mode = GraphicMode.GUI;
    }

    // Set the mode to CAMERA (affected by camera position)
    public static void useCamera() {
        mode = GraphicMode.CAMERA;
    }

    public static GraphicMode getMode() {
        return mode;
    }

    // ====================================== PRIVATE FUNCTIONS ===========================

    private static void applyTransform(Transform camera) {
        // keep track of scale T-T
        float gcScale = (float) gContext.getTransform().getScaleX();

        gContext.setTransform(new AffineTransform());
        if (mode == GraphicMode.CAMERA) {
            gContext.translate(-camera.position.x, -camera.position.y);
        }
        else {
            // GUI mode
            gcScale = 1;
        }

        gContext.scale(gcScale, gcScale); // so sigma bro, kill me
    }

    private static void resetTransform( AffineTransform originalTransform) {
        gContext.setTransform(originalTransform);
    }


    // ====================================== DRAW FUNCTIONS =============================

    public static void drawRect(Transform camera, Transform transform, int radius,
            int borderThickness, Color bodyColor, Color borderColor) {
        AffineTransform originalTransform = gContext.getTransform();
        applyTransform(camera);

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

        resetTransform(originalTransform);
    }

    public static void drawRect(Transform camera, Vector2 start, Vector2 end,
            int thickness, boolean fillBody, Color bodyColor, Color borderColor) {
        AffineTransform originalTransform = gContext.getTransform();
        applyTransform(camera);

        Vector2 dimension = MathUtils.getVector(start, end);
        if (fillBody) {
            gContext.setColor(bodyColor);
            gContext.fillRect(
                    start.getX_int(), start.getY_int(),
                    dimension.getX_int(), dimension.getY_int()
            );
        }

        // Border
        gContext.setColor(borderColor);
        gContext.setStroke(new BasicStroke(thickness));
        gContext.drawRect(
                start.getX_int(), start.getY_int(),
                dimension.getX_int(), dimension.getY_int()
        );

        resetTransform(originalTransform);
    }


    public static void drawPoint(Transform camera, Vector2 position,
            int thickness, boolean isOutline, Color color) {
        AffineTransform originalTransform = gContext.getTransform();
        applyTransform(camera);

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

        resetTransform(originalTransform);
    }



    public static void drawLine(Transform camera,
            Vector2 start, Vector2 end, int thickness, Color color) {
        AffineTransform originalTransform = gContext.getTransform();
        applyTransform(camera);

        gContext.setColor(color);
        gContext.setStroke(new BasicStroke(thickness));
        gContext.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
        gContext.setStroke(new BasicStroke());
        resetTransform(originalTransform);
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
    public static void drawText(Transform camera, Vector2 position, String text) {
        drawTextExt(camera, position, text, gContext.getColor(), 1, 2);
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
    public static void drawTextExt(Transform camera, Vector2 position,
            String text, Color color, int h_alignment, int v_alignment) {
        AffineTransform originalTransform = gContext.getTransform();
        applyTransform(camera);

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

        resetTransform(originalTransform);
    }
}