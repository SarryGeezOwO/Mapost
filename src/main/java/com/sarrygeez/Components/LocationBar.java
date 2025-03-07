package com.sarrygeez.Components;

import com.sarrygeez.Core.Rendering.Camera;
import com.sarrygeez.Core.Rendering.RenderSurface;
import com.sarrygeez.Data.Vector2;
import com.sarrygeez.Logging.Debug;
import com.sarrygeez.Logging.LogLevel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LocationBar extends JPanel {

    public boolean hasInitialized = false;
    private final Camera camera;
    private final RenderSurface surface;

    private final JLabel labelX = new JLabel();
    private final JLabel labelY = new JLabel();

    public LocationBar(RenderSurface surface, Camera camera) {
        this.camera = camera;
        this.surface = surface;
        Dimension size = new Dimension(300, 30);
        setSize(size);

        setBackground(Color.decode("#191a21"));
        setLayout(new MigLayout("fill, insets 5, gap 5"));
        add(labelX, "align center");
        add(labelY, "align center");
    }

    public void init() {
        hasInitialized = true;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                copy();
                surface.requestFocus();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(Color.decode("#252732"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(Color.decode("#191a21"));
            }
        });
    }

    public void update() {
        Vector2 pos = camera.getWorldPosition();
        labelX.setText(String.format("%.2f", pos.x));
        labelY.setText(String.format("%.2f", -pos.y));

        // Verify location, before update location
        Point loc = getLocation();
        int newX = RenderSurface.WIDTH / 2 - 150;
        int newY = RenderSurface.HEIGHT - 40;
        if (loc.x == newX && loc.y == newY) {
            return;
        }
        setLocation(newX, newY);
    }

    private void copy() {
        StringSelection selection = new StringSelection(Vector2.formatStr(camera.getWorldPosition(), true));
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);

        Debug.log("Successfully copied current location!", LogLevel.SUCCESS);
        new Toast("location copied to clipboard",
                new Vector2(15, 35),
                Toast.Duration.SHORT)
                .showToast();
    }
}
