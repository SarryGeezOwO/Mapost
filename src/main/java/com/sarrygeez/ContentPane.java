package com.sarrygeez;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ContentPane extends JPanel {

    private final Camera camera = new Camera();
    private Vector2 lastMousePosition = null;
    private boolean isPanning = false;

    public ContentPane() {

        setDoubleBuffered(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                isPanning = true;
                lastMousePosition = new Vector2(e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPanning = false;
                lastMousePosition = null;
            }
        });


        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isPanning && lastMousePosition != null) {
                    Vector2 currentPos = new Vector2(e.getX(), e.getY());
                    Vector2 dir = new Vector2(
                        currentPos.x - lastMousePosition.x,
                        currentPos.y - lastMousePosition.y
                    );

                    camera.position.x -= dir.x / camera.scale.x * 20;
                    camera.position.y -= dir.y / camera.scale.y * 20;
                    lastMousePosition = currentPos;

                }
                repaint();
            }
        });

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D)g.create();

        for(RectComp rect : Application.GRID_MAP_CONTEXT.objects) {
            //AppGraphics.drawPoint(g2D, camera, t, 10, false, Color.green);
            AppGraphics.drawRect(g2D, camera, rect.transform, rect.radius, rect.borderWidth, rect.background, rect.borderColor);
        }
    }

    public Camera getCamera() {
        return camera;
    }
}
