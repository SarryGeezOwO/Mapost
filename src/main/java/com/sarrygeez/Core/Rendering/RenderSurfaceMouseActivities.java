package com.sarrygeez.Core.Rendering;

import com.sarrygeez.*;
import com.sarrygeez.Core.Actions.Goto;
import com.sarrygeez.Core.Actions.SpawnPost;
import com.sarrygeez.Core.Camera;
import com.sarrygeez.Core.GridMapContext;
import com.sarrygeez.Data.Vector2;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RenderSurfaceMouseActivities implements MouseListener, MouseMotionListener, MouseWheelListener {

    private final RenderSurface surface;
    private final Camera camera;

    private Vector2 lastMousePosition = null;
    public Vector2 mousePosition = new Vector2(); // based actually on this component
    public boolean isPanning = false;

    public RenderSurfaceMouseActivities(RenderSurface surface) {
        this.surface = surface;
        this.camera = surface.getCamera();
    }

    private void repaint() {
        surface.repaint();
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
            menu.add(gotoActionGUI(e.getComponent()), "span, grow");
            menu.add(spawnPostGUI(e.getComponent()), "span, grow");
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
        float zoom = camera.getZoom();
        float add = -e.getWheelRotation() * 0.1f;

        if (zoom+add >= 0.5f && zoom+add <= 2) {
            camera.setZoom(roundToOneDecimal(zoom + add));
            camera.updateCameraMousePosition(mousePosition);
        }
        repaint();
    }

    private float roundToOneDecimal(float value) {
        return Math.round((value * 10)) / 10f;
    }


    // =========================================================================
    // =========================================================================

    /**
     * @return <code>true</code> if a rect has been interacted with.
     *          <code>false</code> otherwise
     */
    public boolean onRightClick() {
        for(RectComponent rect : Application.GRID_MAP_CONTEXT.objects) {

            if (rect.positionInsideBbox(Camera.MOUSE_CAM_POS))
            {
                System.out.println("Rect detected");
                new InspectorView(rect, surface, mousePosition);
                return true;
            }

        }

        return false;
    }


    private JMenuItem gotoActionGUI(Component invoker) {
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
                        -(int)yNum.getValue()
                );
                surface.actionManager.addAction(new Goto(camera, pos));
                whereInput.setVisible(false);
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

    // 💀💀💀 too lazy
    private JMenuItem spawnPostGUI(Component invoker) {
        JMenuItem item = new JMenuItem("Create Post");
        item.addActionListener(e -> {
            JPopupMenu whereInput = new JPopupMenu();
            whereInput.setLayout(new MigLayout("insets 10, FillX, gap 10"));

            JSpinner xNum = new JSpinner();
            JSpinner yNum = new JSpinner();
            Vector2 camPos = Camera.MOUSE_CAM_POS;
            xNum.setValue(camPos.getX_int());
            yNum.setValue(-camPos.getY_int());

            JTextArea message = new JTextArea();
            message.setLineWrap(true);
            message.setWrapStyleWord(true);

            JButton confirm = new JButton("Create");
            confirm.addActionListener(e1 -> {
                Vector2 pos = new Vector2(
                        (int)xNum.getValue(),
                        -(int)yNum.getValue()
                );
                surface.actionManager.addAction(new SpawnPost(pos, message.getText()));
                whereInput.setVisible(false);
            });

            whereInput.add(xNum, "grow, width 120:120:120");
            whereInput.add(yNum, "wrap, grow, width 120:120:120");
            whereInput.add(message, "wrap, span, grow, height 170:170:170");
            whereInput.add(confirm, "span, grow");
            whereInput.show(invoker, mousePosition.getX_int(), mousePosition.getY_int());
        });

        return item;
    }


    // this is for testing shit purposes, this is going to be a setting configuration
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
