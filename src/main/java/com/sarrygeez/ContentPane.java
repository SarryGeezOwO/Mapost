package com.sarrygeez;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ContentPane extends JPanel {

    private final Camera camera = new Camera();
    private Vector2 lastMousePosition = null;
    private boolean isPanning = false;
    private boolean spaceModifier = false;

    public ContentPane() {

        setDoubleBuffered(true);
        setBackground(new Color(80, 80, 95));

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                System.out.println("Space");
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

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

                    camera.position.x -= (float) Math.round(dir.x * camera.panSpeed / camera.scale.x);
                    camera.position.y -= (float) Math.round(dir.y * camera.panSpeed / camera.scale.y);
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
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        AppGraphics.drawLine(g2D, camera, new Vector2(-100000,  0), new Vector2(100000,     0), 1, Color.BLACK); // Horizontal
        AppGraphics.drawLine(g2D, camera, new Vector2(0,        -100000), new Vector2(0,    100000), 1, Color.BLACK); // Vertical

        for(RectComp rect : Application.GRID_MAP_CONTEXT.objects) {
            AppGraphics.drawRect(g2D, camera, rect.transform, rect.radius, rect.borderWidth, rect.background, rect.borderColor);
        }

        g2D.drawString(camera.position.toString(), 10, getHeight() - 12);
        g2D.drawString(String.valueOf(spaceModifier), 10, getHeight() - 32);
        g2D.dispose();
    }

    public Camera getCamera() {
        return camera;
    }


    private void drawGridPoints(Graphics2D g2D) {

    }

}
