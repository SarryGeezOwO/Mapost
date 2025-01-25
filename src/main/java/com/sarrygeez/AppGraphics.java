package com.sarrygeez;

import java.awt.*;
import java.awt.geom.AffineTransform;

@SuppressWarnings("unused")
public class AppGraphics {

    private AppGraphics() {}

    //TODO: Make an option to draw relative to the camera or the window
    //      DrawGUI - elements not affected by camera position or scale
    //      Draw    - elements affected by camera position or scale


    public static void drawRect(Graphics2D g2D, Transform camera, Transform transform, int radius, int borderThickness, Color bodyColor,  Color borderColor) {
        AffineTransform originalTransform = g2D.getTransform();
        g2D.translate(-camera.position.x, -camera.position.y);

        // Border
        g2D.setColor(borderColor);
        g2D.fillRoundRect(
                (int) transform.position.x, (int) transform.position.y,
                (int) (transform.scaleToSize(true)+(borderThickness*2)), (int) (transform.scaleToSize(false)+(borderThickness*2)),
                radius, radius
        );

        // Body
        g2D.setColor(bodyColor);
        g2D.fillRoundRect(
                (int) (transform.position.x+borderThickness), (int) (transform.position.y+borderThickness),
                (int) transform.scaleToSize(true), (int) transform.scaleToSize(false),
                radius, radius
        );

        g2D.setTransform(originalTransform);
    }

    public static void drawGUIRect(Graphics2D g2D, Transform transform, int radius, int borderThickness, Color bodyColor,  Color borderColor) {
        // Border
        g2D.setColor(borderColor);
        g2D.fillRoundRect(
                (int) transform.position.x, (int) transform.position.y,
                (int) (transform.scaleToSize(true)+(borderThickness*2)), (int) (transform.scaleToSize(false)+(borderThickness*2)),
                radius, radius
        );

        // Body
        g2D.setColor(bodyColor);
        g2D.fillRoundRect(
                (int) (transform.position.x+borderThickness), (int) (transform.position.y+borderThickness),
                (int) transform.scaleToSize(true), (int) transform.scaleToSize(false),
                radius, radius
        );
    }


    public static void drawPoint(Graphics2D g2D, Transform camera, Transform transform, int thickness, boolean isOutline, Color color) {
        g2D.setColor(color);

        AffineTransform originalTransform = g2D.getTransform();
        g2D.translate(-camera.position.x, -camera.position.y);
        int offset = thickness/2;

        if (isOutline) {
            g2D.drawOval((int) transform.position.x-offset, (int) transform.position.y-offset, thickness, thickness);
            return;
        }

        g2D.fillOval((int) transform.position.x-offset, (int) transform.position.y-offset, thickness, thickness);
        g2D.setTransform(originalTransform);
    }

    public static void drawGUIPoint(Graphics2D g2D, Transform transform, int thickness, boolean isOutline, Color color) {
        g2D.setColor(color);
        int offset = thickness/2;
        if (isOutline) {
            g2D.drawOval((int) transform.position.x-offset, (int) transform.position.y-offset, thickness, thickness);
            return;
        }

        g2D.fillOval((int) transform.position.x-offset, (int) transform.position.y-offset, thickness, thickness);

    }


    public static void drawLine(Graphics2D g2D, Transform camera, Vector2 start, Vector2 end, int thickness, Color color) {
        g2D.setColor(color);

        AffineTransform originalTransform = g2D.getTransform();
        g2D.translate(-camera.position.x, -camera.position.y);

        g2D.setStroke(new BasicStroke(thickness));
        g2D.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);

        g2D.setTransform(originalTransform);
        g2D.setStroke(new BasicStroke());
    }

}
