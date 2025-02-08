package com.sarrygeez.Core.Rendering;

import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.sarrygeez.*;
import com.sarrygeez.Core.Actions.Action;
import com.sarrygeez.Core.Actions.ActionManager;
import com.sarrygeez.Core.Inputs.RenderSurfaceKeyInputs;
import com.sarrygeez.Core.Inputs.RenderSurfaceMouseActivities;
import com.sarrygeez.Data.Vector2;
import com.sarrygeez.Debug.Debug;
import com.sarrygeez.Debug.LogLevel;
import com.sarrygeez.Tools.AppGraphics;

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
    public final ActionManager actionManager = new ActionManager();

    public static Color guideLineColor = Color.decode("#25252F");
    public static int guideLineColorAlpha = 50;
    public static GuidelineType guidelineType = GuidelineType.DOTS;

    public static int WIDTH = 0;
    public static int HEIGHT = 0;

    public boolean isSelectionActive = false;
    public Vector2 selectionStart = null;
    public Vector2 selectionEnd = null;

    private final RenderSurfaceMouseActivities mouseActivities = new RenderSurfaceMouseActivities(this);
    private final RenderSurfaceKeyInputs keyInputs;

    public RenderSurface(RenderSurfaceKeyInputs keyInputs) {
        this.keyInputs = keyInputs;

        setDoubleBuffered(true);
        setBackground(new Color(80, 80, 95));

        addMouseListener(mouseActivities);
        addMouseMotionListener(mouseActivities);
        addMouseWheelListener(mouseActivities);
        requestFocus();

        // Logical update
        new Timer(0, e -> update()).start();

        WIDTH = getWidth();
        HEIGHT = getHeight();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D)g.create();
        g2D.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 14));
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Guidelines duh...
        drawGridGuidelines(g2D, guidelineType);

        //Apply zoom scale
        g2D.scale(camera.getZoom(), camera.getZoom());

        AppGraphics.setGContext(g2D); // Update Graphical context
        AppGraphics.useCamera(); // ========= CAMERA BASED RENDER

        drawCartesianLines();
        updateRectComponents(g2D);
        drawSelection(selectionStart, selectionEnd);

        AppGraphics.useGUI();
        drawDebugTexts(g2D);
        g2D.dispose();
    }

    private void drawSelection(Vector2 start, Vector2 end) {
        if (!isSelectionActive) {
            return;
        }
        if (start == null && end == null) {
            return;
        }
        AppGraphics.drawRect(
                camera, start, end, 2, true,
                new Color(162, 84, 222, 70), Color.decode("#862bcc"));
    }

    private void drawCartesianLines() {
        Color midLineCol = Color.decode("#25252F");
        AppGraphics.drawLine(camera, new Vector2(-100000,       0), new Vector2(100000,    0), 1,  midLineCol); // Horizontal
        AppGraphics.drawLine(camera, new Vector2(0,       -100000), new Vector2(0,     100000), 1, midLineCol); // Vertical
    }

    private void updateRectComponents(Graphics2D g2D) {
        for(RectComponent rect : Application.GRID_MAP_CONTEXT.objects) {
            rect.update();
            rect.draw(g2D, camera);
        }
    }

    private void drawDebugTexts(Graphics2D g2D) {
        g2D.setColor(Color.YELLOW);
        AppGraphics.drawText(camera, new Vector2(10, getHeight() - 92), "Mouse(Grid) pos:      " + GridMapContext.MOUSE_POSITION.toString());
        AppGraphics.drawText(camera, new Vector2(10, getHeight() - 72), "Mouse(Camera) pos:    " + Camera.MOUSE_CAM_POS.toString());
        AppGraphics.drawText(camera, new Vector2(10, getHeight() - 52), "Mouse(Component) pos: " + mouseActivities.mousePosition.toString());
        AppGraphics.drawText(camera, new Vector2(10, getHeight() - 32), "camera pos:           " + camera.getCartesianPosition().toString());
        AppGraphics.drawText(camera, new Vector2(10, getHeight() - 12), "Camera Zoom:          " + camera.getZoom());
    }

    private void update() {

        // Flush out 1 action per draw call
        if (!actionManager.getActionQueue().isEmpty()) {
            Action act = actionManager.triggerAction();
            Debug.log("Action Triggered: " + act.name(), LogLevel.SUCCESS);
        }
    }


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
        int startX = -(camPos.getX_int() % cellSize + cellSize) % cellSize;
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


    public Camera getCamera() {
        return camera;
    }

    public RenderSurfaceKeyInputs getKeyInputs() {
        return keyInputs;
    }
}
