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
                updateCameraPosition(new Vector2(e.getX(), e.getY()));

                if (isPanning && lastMousePosition != null) {
                    mousePosition = new Vector2(e.getX(), e.getY());
                    Vector2 dir = new Vector2(
                            mousePosition.x - lastMousePosition.x,
                            mousePosition.y - lastMousePosition.y
                    );

                    camera.position.x -= (float) Math.round(dir.x * camera.panSpeed / camera.scale.x);
                    camera.position.y -= (float) Math.round(dir.y * (camera.panSpeed/2) / camera.scale.y);
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
                updateCameraPosition(mousePosition);
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
        drawGridLines(g2D);

        AppGraphics.drawLine(g2D, camera, new Vector2(-100000,       0), new Vector2(100000,    0), 1, Color.BLACK); // Horizontal
        AppGraphics.drawLine(g2D, camera, new Vector2(0,       -100000), new Vector2(0,     100000), 1, Color.BLACK); // Vertical

        for(RectComp rect : Application.GRID_MAP_CONTEXT.objects) {
            AppGraphics.drawRect(g2D, camera, rect.transform, rect.radius, rect.borderWidth, rect.background, rect.borderColor);
        }

        g2D.setColor(Color.BLACK);
        g2D.drawString("Mouse(Grid) pos:   " + GridMapContext.MOUSE_POSITION.toString(), 10, getHeight() - 52);
        g2D.drawString("Mouse(Camera) pos:   " + Camera.MOUSE_CAM_POS.toString(), 10, getHeight() - 32);
        g2D.drawString("camera pos:          " + camera.position.toString(), 10, getHeight() - 12);
        g2D.dispose();
    }

    public Camera getCamera() {
        return camera;
    }

    public void updateCameraPosition(Vector2 basePos) {
        Camera.MOUSE_CAM_POS.x = basePos.getX_int() + camera.position.x;
        Camera.MOUSE_CAM_POS.y = basePos.getY_int() + camera.position.y;
    }


    private void drawGridLines(Graphics2D g2d) {
        g2d.setColor(new Color(25, 25, 25, 70));
        int width = getWidth();
        int height = getHeight();
        int cellSize = GridMapContext.CELL_SIZE;
        Vector2 camPos = camera.position;

        // Calculate the top-left corner in terms of grid
        int startX = -(camPos.getX_int() % cellSize + cellSize) % cellSize; // Ensures positive values
        int startY = -(camPos.getY_int() % cellSize + cellSize) % cellSize;

        // Draw vertical lines
        for (int x = startX; x < width; x += cellSize) {
            g2d.drawLine(x, 0, x, height);
        }

        // Draw horizontal lines
        for (int y = startY; y < height; y += cellSize) {
            g2d.drawLine(0, y, width, y);
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
                int xPos = (int)xNum.getValue();
                int yPos = (int)yNum.getValue();
                updateCameraPosition(new Vector2(xPos, yPos));
                repaint();
                revalidate();
            });

            whereInput.add(new JLabel("X: "));
            whereInput.add(xNum, "wrap, grow, width 80:80:80");

            whereInput.add(new JLabel("Y: "));
            whereInput.add(yNum, "wrap, grow, width 80:80:80");
            whereInput.add(confirm, "span, grow");
            whereInput.show(invoker, mousePosition.getX_int(), mousePosition.getY_int());
        });

        return item;
    }
}
