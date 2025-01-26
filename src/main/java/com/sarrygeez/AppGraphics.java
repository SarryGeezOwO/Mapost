package com.sarrygeez;

import java.awt.*;
import java.awt.geom.AffineTransform;

@SuppressWarnings("unused")
public class AppGraphics {

    public enum GraphicMode {
        CAMERA, GUI
    }

    private static GraphicMode mode = GraphicMode.CAMERA;

    private AppGraphics() {}
    // NOTE: an option to draw relative to the camera or the window
    //       DrawGUI - elements not affected by camera position or scale
    //       Draw    - elements affected by camera position or scale

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

    private static void applyTransform(Graphics2D g2D, Transform camera) {
        if (mode == GraphicMode.CAMERA) {
            g2D.translate(-camera.position.x, -camera.position.y);
        }
    }

    private static void resetTransform(Graphics2D g2D, AffineTransform originalTransform) {
        g2D.setTransform(originalTransform);
    }


    // ====================================== RECT ===========================

    private static void drawRectangle(Graphics2D g2D, Transform transform, int radius,
            int borderThickness, Color bodyColor, Color borderColor) {
        // Border
        g2D.setColor(borderColor);
        g2D.fillRoundRect(
                (int) transform.position.x, (int) transform.position.y,
                (int) (transform.scaleToSize(true) + (borderThickness * 2)),
                (int) (transform.scaleToSize(false) + (borderThickness * 2)),
                radius, radius
        );

        // Body
        g2D.setColor(bodyColor);
        g2D.fillRoundRect(
                (int) (transform.position.x + borderThickness),
                (int) (transform.position.y + borderThickness),
                (int) transform.scaleToSize(true),
                (int) transform.scaleToSize(false),
                radius, radius
        );
    }

    public static void drawRect(Graphics2D g2D, Transform camera, Transform transform,
            int radius, int borderThickness, Color bodyColor, Color borderColor) {
        AffineTransform originalTransform = g2D.getTransform();
        applyTransform(g2D, camera);
        drawRectangle(g2D, transform, radius, borderThickness, bodyColor, borderColor);
        resetTransform(g2D, originalTransform);
    }

    public static void drawGUIRect(Graphics2D g2D, Transform transform,
            int radius, int borderThickness, Color bodyColor, Color borderColor) {
        AffineTransform originalTransform = g2D.getTransform();
        if (mode == GraphicMode.GUI) {
            drawRectangle(g2D, transform, radius, borderThickness, bodyColor, borderColor);
        }
        resetTransform(g2D, originalTransform);
    }


    // ====================================== CIRCLE ===========================

    private static void drawCircle(Graphics2D g2D, Vector2 position,
            int thickness, boolean isOutline, Color color) {
        g2D.setColor(color);
        int offset = thickness / 2;

        if (isOutline) {
            g2D.drawOval((int)position.x - offset,
                    (int)position.y - offset,
                    thickness, thickness);
        } else {
            g2D.fillOval((int)position.x - offset,
                    (int)position.y - offset,
                    thickness, thickness);
        }
    }

    public static void drawPoint(Graphics2D g2D, Transform camera, Vector2 position,
            int thickness, boolean isOutline, Color color) {
        AffineTransform originalTransform = g2D.getTransform();
        applyTransform(g2D, camera);
        drawCircle(g2D, position, thickness, isOutline, color);
        resetTransform(g2D, originalTransform);
    }

    public static void drawGUIPoint(Graphics2D g2D, Vector2 position,
            int thickness, boolean isOutline, Color color) {
        AffineTransform originalTransform = g2D.getTransform();
        if (mode == GraphicMode.GUI) {
            drawCircle(g2D, position, thickness, isOutline, color);
        }
        resetTransform(g2D, originalTransform);
    }


    // ====================================== LINE ===========================

    private static void drawLineRaw(Graphics2D g2D,
            Vector2 start, Vector2 end, int thickness, Color color) {
        g2D.setColor(color);
        g2D.setStroke(new BasicStroke(thickness));
        g2D.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
        g2D.setStroke(new BasicStroke());
    }

    public static void drawLine(Graphics2D g2D, Transform camera,
            Vector2 start, Vector2 end, int thickness, Color color) {
        AffineTransform originalTransform = g2D.getTransform();
        applyTransform(g2D, camera);
        drawLineRaw(g2D, start, end, thickness, color);
        resetTransform(g2D, originalTransform);
    }

    public static void drawGUILine(Graphics2D g2D,
            Vector2 start, Vector2 end, int thickness, Color color) {
        AffineTransform originalTransform = g2D.getTransform();
        if (mode == GraphicMode.GUI) {
            drawLineRaw(g2D, start, end, thickness, color);
        }
        resetTransform(g2D, originalTransform);
    }



    // ====================================== STRING ===========================


    /**
     * HorizontalAlignment:<br>
     *      1 = left<br>
     *      2 = right<br>
     *      3 = center
     *      <br><br>
     * VerticalAlignment:<br>
     *      1 = top<br>
     *      2 = bottom<br>
     *      3 = center
     */
    private static void drawString(Graphics2D g2D, Vector2 position, String text, Color color, int h_alignment, int v_alignment) {
        g2D.setColor(color);
        FontMetrics fm = g2D.getFontMetrics();
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
            g2D.drawString(line, offsetX, offsetY);
            offsetY += fontHeight;
        }
    }

    public static void drawText(Graphics2D g2D, Transform camera, Vector2 position, String text, Color color, int h_alignment, int v_alignment) {
        AffineTransform originalTransform = g2D.getTransform();
        applyTransform(g2D, camera);
        drawString(g2D, position, text, color, h_alignment, v_alignment);
        resetTransform(g2D, originalTransform);
    }

    public static void drawGUIText(Graphics2D g2D, Vector2 position, String text, Color color, int h_alignment, int v_alignment) {
        AffineTransform originalTransform = g2D.getTransform();
        if (mode == GraphicMode.GUI) {
            drawString(g2D, position, text, color, h_alignment, v_alignment);
        }
        resetTransform(g2D, originalTransform);
    }

}
