package com.sarrygeez;

import com.formdev.flatlaf.fonts.inter.FlatInterFont;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ContentPane extends JPanel {

    private final Camera camera = new Camera();
    private Vector2 lastMousePosition = null;
    private Vector2 mousePosition = new Vector2();
    private boolean isPanning = false;

    public ContentPane() {

        setDoubleBuffered(true);
        setBackground(new Color(80, 80, 95));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    isPanning = true;
                    lastMousePosition = new Vector2(e.getX(), e.getY());
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == 3) {
                    onRightClick();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 1) {
                    isPanning = false;
                    lastMousePosition = null;
                }
            }
        });


        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                GridMapContext.MOUSE_POSITION.x = e.getXOnScreen();
                GridMapContext.MOUSE_POSITION.y = e.getYOnScreen();
                Camera.MOUSE_CAM_POS.x = e.getX() + camera.position.x;
                Camera.MOUSE_CAM_POS.y = e.getY() + camera.position.y;

                if (isPanning && lastMousePosition != null) {
                    mousePosition = new Vector2(e.getX(), e.getY());
                    Vector2 dir = new Vector2(
                            mousePosition.x - lastMousePosition.x,
                            mousePosition.y - lastMousePosition.y
                    );

                    camera.position.x -= (float) Math.round(dir.x * camera.panSpeed / camera.scale.x);
                    camera.position.y -= (float) Math.round(dir.y * camera.panSpeed / camera.scale.y);
                    lastMousePosition = mousePosition;

                }
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                GridMapContext.MOUSE_POSITION.x = e.getXOnScreen();
                GridMapContext.MOUSE_POSITION.y = e.getYOnScreen();
                Camera.MOUSE_CAM_POS.x = e.getX() + camera.position.x;
                Camera.MOUSE_CAM_POS.y = e.getY() + camera.position.y;
                mousePosition.x = e.getX();
                mousePosition.y = e.getY();
                repaint();
            }
        });

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D)g.create();
        g2D.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 14));
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        AppGraphics.drawLine(g2D, camera, new Vector2(-100000,  0), new Vector2(100000,     0), 1, Color.BLACK); // Horizontal
        AppGraphics.drawLine(g2D, camera, new Vector2(0,        -100000), new Vector2(0,    100000), 1, Color.BLACK); // Vertical

        for(RectComp rect : Application.GRID_MAP_CONTEXT.objects) {
            AppGraphics.drawRect(g2D, camera, rect.transform, rect.radius, rect.borderWidth, rect.background, rect.borderColor);
        }

        g2D.setColor(Color.BLACK);
        g2D.drawString("Mouse(Camera) pos:   " + Camera.MOUSE_CAM_POS.toString(), 10, getHeight() - 32);
        g2D.drawString("camera pos:          " + camera.position.toString(), 10, getHeight() - 12);
        g2D.dispose();
    }

    public Camera getCamera() {
        return camera;
    }


    private void drawGridPoints(Graphics2D g2D) {

    }


    public void onRightClick() {
        for(RectComp rect : Application.GRID_MAP_CONTEXT.objects) {

            if (rect.positionInsideBbox(Camera.MOUSE_CAM_POS))
            {
                System.out.println("Rect detected");
                new InspectorView(rect, this, mousePosition);
                return;
            }

        }
    }
}
