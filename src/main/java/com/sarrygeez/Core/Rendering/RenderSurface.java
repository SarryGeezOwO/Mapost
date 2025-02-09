package com.sarrygeez.Core.Rendering;

import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.sarrygeez.*;
import com.sarrygeez.Components.LocationBar;
import com.sarrygeez.Components.RectComponent;
import com.sarrygeez.Core.Actions.Action;
import com.sarrygeez.Core.Actions.ActionManager;
import com.sarrygeez.Core.Inputs.GlobalInput;
import com.sarrygeez.Core.Inputs.RenderSurfaceMouseActivities;
import com.sarrygeez.Data.Vector2;
import com.sarrygeez.Debug.Debug;
import com.sarrygeez.Debug.LogLevel;
import com.sarrygeez.Tools.AppGraphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

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

    public static Color guideLineColor = new Color(70, 70, 80);
    public static int guideLineColorAlpha = 200;
    public static GuidelineType guidelineType = GuidelineType.LINES;

    public static int WIDTH = 0;
    public static int HEIGHT = 0;

    public boolean isSelectionActive = false;
    public Vector2 selectionStart = null;
    public Vector2 selectionEnd = null;

    private final LocationBar locationBar = new LocationBar(this, camera);

    public RenderSurface() {

        setDoubleBuffered(true);
        setBackground(new Color(80, 80, 95));
        setLayout(null);

        RenderSurfaceMouseActivities mouseActivities = new RenderSurfaceMouseActivities(this);
        addMouseListener(mouseActivities);
        addMouseMotionListener(mouseActivities);
        addMouseWheelListener(mouseActivities);
        requestFocus();

        // Logical update
        new Timer(0, e -> update()).start();
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

        if(GlobalInput.isActivated(KeyEvent.VK_ALT)) {
            // Show mouse coordinate ( world )
            AppGraphics.drawText(camera,
                Vector2.add(Camera.MOUSE_CAM_POS, new Vector2(5, -3)),
                Vector2.formatStr(camera.getWorldMousePosition(), true));
        }

        AppGraphics.useGUI();
        AppGraphics.drawTextExt(
                camera, new Vector2(WIDTH/2, HEIGHT-50),
                "x         LOCATION         y", Color.WHITE, 3, 3
        );
        drawDebugTexts(g2D);
        g2D.dispose();
    }

    private void drawSelection(Vector2 start, Vector2 end) {
        if (!isSelectionActive) {
            return;
        }
        if (start == null || end == null) {
            return;
        }
        AppGraphics.drawRect(
                camera, start, end, 10, 2, true,
                new Color(162, 84, 222, 70), Color.decode("#862bcc"));
    }

    private void drawCartesianLines() {
        Color midLineCol = Color.decode("#25252F");
        AppGraphics.drawLine(camera, new Vector2(-100000,       0), new Vector2(100000,    0), 2,  midLineCol); // Horizontal
        AppGraphics.drawLine(camera, new Vector2(0,       -100000), new Vector2(0,     100000), 2, midLineCol); // Vertical
    }

    private void updateRectComponents(Graphics2D g2D) {
        for(RectComponent rect : Application.GRID_MAP_CONTEXT.objects) {
            rect.update();
            rect.draw(g2D, camera);
        }
    }

    private void drawDebugTexts(Graphics2D g2D) {
        g2D.setColor(Color.YELLOW);
        AppGraphics.drawText(camera, new Vector2(10, getHeight() - 12), "Camera Zoom:      " + camera.getZoom());
    }

    // =================== LOGICAL UPDATE =======================
    private void update() {
        WIDTH = getWidth();
        HEIGHT = getHeight();
        if (!locationBar.hasInitialized) {
            locationBar.init();
            add(locationBar);
        }
        locationBar.update();

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
        int cellSize = (int)(GridMapContext.CELL_SIZE * camera.getZoom());
        Vector2 camPos = camera.position;

        // Calculate the top-left corner in terms of grid
        int startX = -(camPos.getX_int() % cellSize + cellSize) % cellSize;
        int startY = -(camPos.getY_int() % cellSize + cellSize) % cellSize;

        if (type == GuidelineType.DOTS) {
            drawGuideDots(g2d, startX, WIDTH, cellSize, startY, HEIGHT);
        }
        else if (type == GuidelineType.LINES) {
            drawGuideLines(g2d, startX, WIDTH, cellSize, HEIGHT, startY);
        }
        else {
            drawGuideDots(g2d, startX, WIDTH, cellSize, startY, HEIGHT);
            drawGuideLines(g2d, startX, WIDTH, cellSize, HEIGHT, startY);
        }

    }

    private void drawGuideDots(Graphics2D g2d, int startX, int width, int cellSize, int startY, int height) {
        // GridPoints
        for (int x = startX; x < width; x += cellSize) {
            for (int y = startY; y < height; y += cellSize) {
                int size = (camera.getZoom() > 1.25) ? 6 : 4;
                g2d.fillOval(x-(size/2), y-(size/2), size, size);
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
}
