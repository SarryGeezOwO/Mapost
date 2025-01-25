package com.sarrygeez;

import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ContentPane extends JPanel {

    private final Camera camera = new Camera();
    private Vector2 lastMousePosition = null;
    private Vector2 mousePosition = new Vector2(); // based actually on this component
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
                if (e.getButton() != 3) { // Check for a right click
                    return;
                }

                boolean flag = onRightClick();
                if (!flag) {
                    // open context menu
                    Vector2 screenPos = mousePosition;

                    JPopupMenu menu = new JPopupMenu();
                    menu.setLayout(new MigLayout("FillX, FlowY, insets 0, gap 5"));
                    menu.add(gotoAction(e.getComponent()));

                    menu.show(e.getComponent(),screenPos.getX_int(), screenPos.getY_int());
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
                camera.updateCameraMousePosition(new Vector2(e.getX(), e.getY()));

                if (isPanning && lastMousePosition != null) {
                    mousePosition = new Vector2(e.getX(), e.getY());
                    Vector2 dir = new Vector2(
                            mousePosition.x - lastMousePosition.x,
                            mousePosition.y - lastMousePosition.y
                    );

                    camera.position.x -= dir.x * camera.panSpeed / camera.scale.x;
                    camera.position.y -= dir.y * (camera.panSpeed/2) / camera.scale.y;
                    lastMousePosition = mousePosition;

                }
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mousePosition.x = e.getX();
                mousePosition.y = e.getY();
                GridMapContext.MOUSE_POSITION.x = e.getXOnScreen();
                GridMapContext.MOUSE_POSITION.y = e.getYOnScreen();
                camera.updateCameraMousePosition(mousePosition);
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
        drawGridGuidelines(g2D, false);

        AppGraphics.useCamera(); // ========= CAMERA BASED RENDERS
        AppGraphics.drawLine(g2D, camera, new Vector2(-100000,       0), new Vector2(100000,    0), 1, Color.BLACK); // Horizontal
        AppGraphics.drawLine(g2D, camera, new Vector2(0,       -100000), new Vector2(0,     100000), 1, Color.BLACK); // Vertical
        AppGraphics.drawLine(g2D, camera, new Vector2(0, 0), Camera.MOUSE_CAM_POS, 5, Color.YELLOW);

        for(RectComp rect : Application.GRID_MAP_CONTEXT.objects) {
            rect.update();
            rect.draw(g2D, camera);
        }

        AppGraphics.useGUI(); // ============== GUI BASED RENDERS
        g2D.setColor(Color.BLACK);
        g2D.drawString("Mouse(Component) pos:   " + mousePosition.toString(), 10, getHeight() - 72);
        g2D.drawString("Mouse(Grid) pos:   " + GridMapContext.MOUSE_POSITION.toString(), 10, getHeight() - 52);
        g2D.drawString("Mouse(Camera) pos:   " + Camera.MOUSE_CAM_POS.toString(), 10, getHeight() - 32);
        g2D.drawString("camera pos:          " + camera.getCartesianPosition().toString(), 10, getHeight() - 12);
        g2D.dispose();
    }

    public Camera getCamera() {
        return camera;
    }


    private void drawGridGuidelines(Graphics2D g2d, boolean isLines) {
        g2d.setColor(new Color(25, 25, 25, 50));
        int width = getWidth();
        int height = getHeight();
        int cellSize = 64;
        Vector2 camPos = camera.position;

        // Calculate the top-left corner in terms of grid
        int startX = -(camPos.getX_int() % cellSize + cellSize) % cellSize; // Ensures positive values
        int startY = -(camPos.getY_int() % cellSize + cellSize) % cellSize;

        if (isLines) {
            // Draw vertical lines
            for (int x = startX; x < width; x += cellSize) {
                g2d.drawLine(x, 0, x, height);
            }

            // Draw horizontal lines
            for (int y = startY; y < height; y += cellSize) {
                g2d.drawLine(0, y, width, y);
            }
        }
        else {
            // GridPoints
            for (int x = startX; x < width; x += cellSize) {
                for (int y = startY; y < height; y += cellSize) {
                    g2d.fillOval(x-3, y-3, 6, 6);
                }
            }
        }
    }


    /**
     * @return <code>true</code> if a rect has been interacted with.
     *          <code>false</code> otherwise
     */
    public boolean onRightClick() {
        for(RectComp rect : Application.GRID_MAP_CONTEXT.objects) {

            if (rect.positionInsideBbox(Camera.MOUSE_CAM_POS))
            {
                System.out.println("Rect detected");
                new InspectorView(rect, this, mousePosition);
                return true;
            }

        }

        return false;
    }


    private JMenuItem gotoAction(Component invoker) {
        JMenuItem item = new JMenuItem("Go to");
        item.addActionListener(e -> {
            JPopupMenu whereInput = new JPopupMenu();
            whereInput.setLayout(new MigLayout("insets 10, gap 10"));

            JSpinner xNum = new JSpinner();
            JSpinner yNum = new JSpinner();

            JButton confirm = new JButton("confirm");
            confirm.addActionListener(e1 -> {
                Vector2 pos = new Vector2(
                        (int)xNum.getValue(),
                        (int)yNum.getValue()
                );
                gotoConfirm(pos, whereInput);
            });

            whereInput.add(new JLabel("X: "));
            whereInput.add(xNum, "wrap, grow, width 120:120:120");

            whereInput.add(new JLabel("Y: "));
            whereInput.add(yNum, "wrap, grow, width 120:120:120");
            whereInput.add(confirm, "span, grow");
            whereInput.show(invoker, mousePosition.getX_int(), mousePosition.getY_int());
        });

        return item;
    }

    private void gotoConfirm(Vector2 pos, JPopupMenu caller) {
        Vector2 coord = Application.toCartesianCoordinate(pos);

        camera.setPosition(coord);
        revalidate();
        repaint();

        caller.setVisible(false);
    }
}
