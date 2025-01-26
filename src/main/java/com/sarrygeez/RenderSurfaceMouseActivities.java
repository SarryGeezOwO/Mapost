package com.sarrygeez;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RenderSurfaceMouseActivities implements MouseListener, MouseMotionListener, MouseWheelListener {

    private final RenderSurface surface;
    private final Camera camera;

    private Vector2 lastMousePosition = null;
    private Vector2 mousePosition = new Vector2(); // based actually on this component
    private boolean isPanning = false;

    public RenderSurfaceMouseActivities(RenderSurface surface) {
        this.surface = surface;
        this.camera = surface.getCamera();
    }

    private void repaint() {
        surface.repaint();
    }

    private void revalidate() {
        surface.revalidate();
    }



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
            menu.add(gotoAction(e.getComponent()), "span, grow");
            menu.add(changeGuidelineAction(e.getComponent()), "span, grow");

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

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

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

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        System.out.println(e.getPreciseWheelRotation());
    }


    // =========================================================================
    // =========================================================================

    /**
     * @return <code>true</code> if a rect has been interacted with.
     *          <code>false</code> otherwise
     */
    public boolean onRightClick() {
        for(RectComp rect : Application.GRID_MAP_CONTEXT.objects) {

            if (rect.positionInsideBbox(Camera.MOUSE_CAM_POS))
            {
                System.out.println("Rect detected");
                new InspectorView(rect, surface, mousePosition);
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


    private JMenuItem changeGuidelineAction(Component invoker) {
        JMenuItem change = new JMenuItem("Change Guideline");
        change.addActionListener(e -> {
            JPopupMenu menu = new JPopupMenu();
            menu.setLayout(new MigLayout("FillX, FlowY"));
            for(RenderSurface.GuidelineType type : RenderSurface.GuidelineType.values()) {
                JButton btn = new JButton(type.name().toLowerCase());
                btn.addActionListener(e1 -> {
                    RenderSurface.guidelineType = type;
                    repaint();
                });

                menu.add(btn, "span, grow");
            }
            menu.show(invoker, mousePosition.getX_int(), mousePosition.getY_int());
        });
        return change;
    }
}
