package com.sarrygeez;

import com.formdev.flatlaf.fonts.inter.FlatInterFont;

import javax.swing.*;
import java.awt.*;

/*
    A very unclear class object, literally breaking every rule of
    code design in mankind. Well it's not like someone will be reading this anyway ¯\_(ツ)_/¯
*/

public class RenderSurface extends JPanel {

    @SuppressWarnings("unused")
    public enum GuidelineType {
        DOTS, LINES, BOTH
    }

    public final Camera camera = new Camera();

    public static Color guideLineColor = Color.decode("#25252F");
    public static int guideLineColorAlpha = 50;
    public static GuidelineType guidelineType = GuidelineType.DOTS;

    public RenderSurface() {

        setDoubleBuffered(true);
        setBackground(new Color(80, 80, 95));

        RenderSurfaceMouseActivities mouseActivities = new RenderSurfaceMouseActivities(this);
        addMouseListener(mouseActivities);
        addMouseMotionListener(mouseActivities);
        addMouseWheelListener(mouseActivities);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D)g.create();
        g2D.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 14));
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawGridGuidelines(g2D, guidelineType);

        AppGraphics.setGContext(g2D); // Update Graphical context
        AppGraphics.useCamera(); // ========= CAMERA BASED RENDER

        Color midLineCol = Color.decode("#25252F");
        AppGraphics.drawLine(camera, new Vector2(-100000,       0), new Vector2(100000,    0), 1,  midLineCol); // Horizontal
        AppGraphics.drawLine(camera, new Vector2(0,       -100000), new Vector2(0,     100000), 1, midLineCol); // Vertical

        g2D.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 20));
        AppGraphics.drawTextExt(camera, new Vector2(0, -300), "Welcome to Mapost!", midLineCol, 3, 3);
        resetG2DFont(g2D);

        for(RectComp rect : Application.GRID_MAP_CONTEXT.objects) {
            rect.update();
            rect.draw(g2D, camera);
        }

        AppGraphics.useGUI(); // ============== GUI BASED RENDERS
        AppGraphics.drawText(camera, new Vector2(10, getHeight() - 52), "Mouse(Grid) pos:   " + GridMapContext.MOUSE_POSITION.toString());
        AppGraphics.drawText(camera, new Vector2(10, getHeight() - 32), "Mouse(Camera) pos: " + Camera.MOUSE_CAM_POS.toString());
        AppGraphics.drawText(camera, new Vector2(10, getHeight() - 12), "camera pos:        " + camera.getCartesianPosition().toString());
        g2D.dispose();
    }

    public Camera getCamera() {
        return camera;
    }

    public void resetG2DFont(Graphics2D g2d) {
        g2d.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 14));
    }


    @SuppressWarnings("All")
    private void drawGridGuidelines(Graphics2D g2d, GuidelineType type) {
        g2d.setColor(new Color(
                guideLineColor.getRed(),
                guideLineColor.getGreen(),
                guideLineColor.getBlue(),
                guideLineColorAlpha
        ));
        int width = getWidth();
        int height = getHeight();
        int cellSize = 64;
        Vector2 camPos = camera.position;

        // Calculate the top-left corner in terms of grid
        int startX = -(camPos.getX_int() % cellSize + cellSize) % cellSize; // Ensures positive values
        int startY = -(camPos.getY_int() % cellSize + cellSize) % cellSize;

        if (type == GuidelineType.DOTS) {
            drawGuideDots(g2d, startX, width, cellSize, startY, height);
        }
        else if (type == GuidelineType.LINES) {
            drawGuideLines(g2d, startX, width, cellSize, height, startY);
        }
        else {
            drawGuideDots(g2d, startX, width, cellSize, startY, height);
            drawGuideLines(g2d, startX, width, cellSize, height, startY);
        }

    }

    private void drawGuideDots(Graphics2D g2d, int startX, int width, int cellSize, int startY, int height) {
        // GridPoints
        for (int x = startX; x < width; x += cellSize) {
            for (int y = startY; y < height; y += cellSize) {
                g2d.fillOval(x-3, y-3, 6, 6);
            }
        }
    }

    private void drawGuideLines(Graphics2D g2d, int startX, int width, int cellSize, int height, int startY) {
        // Draw vertical lines
        for (int x = startX; x < width; x += cellSize) {
            g2d.drawLine(x, 0, x, height);
        }

        // Draw horizontal lines
        for (int y = startY; y < height; y += cellSize) {
            g2d.drawLine(0, y, width, y);
        }
    }
}
