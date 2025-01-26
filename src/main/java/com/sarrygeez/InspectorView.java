package com.sarrygeez;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class InspectorView extends JPopupMenu {

    public InspectorView(RectComp rect, Component invoker, Vector2 position) {
        setLayout(new MigLayout("fillX, flowY"));
        setLocation(GridMapContext.getMouseX_int(), GridMapContext.getMouseY_int());

        float width = rect.transform.scaleToSize(true);
        float height = rect.transform.scaleToSize(false);
        String widthStr =   (width % 1 == 0) ? String.format("%.0f", width) : String.valueOf(width);
        String heightStr =  (height % 1 == 0) ? String.format("%.0f", height) : String.valueOf(height);

        add(new JLabel("World Position: " + rect.transform.position.toString()));
        add(new JLabel("Scale: " + rect.transform.scale.toString()));
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(new JLabel("Width: " + widthStr));
        add(new JLabel("Height: " + heightStr));
        show(invoker, (int)position.x, (int)position.y);
    }

}
