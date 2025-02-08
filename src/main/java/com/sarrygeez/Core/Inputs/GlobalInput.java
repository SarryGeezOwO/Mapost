package com.sarrygeez.Core.Inputs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GlobalInput {

    private static GlobalInput instance;
    private static JFrame frame;

    private static final ArrayList<MouseActionAdapter> mouseActions = new ArrayList<>();

    private GlobalInput() {
        // Global mouse listener
        final AWTEventListener listener = event -> {

            MouseEvent me = ( MouseEvent ) event;
            Component c = me.getComponent ();

            // Ignoring mouse events from any other frame
            if ( SwingUtilities.getWindowAncestor ( c ) == frame )
            {
                for(MouseActionAdapter adapter : mouseActions) {
                    if ( event.getID () == MouseEvent.MOUSE_PRESSED ) {
                        adapter.onMousePressed();
                    }
                    if ( event.getID () == MouseEvent.MOUSE_CLICKED ) {
                        adapter.onMouseClick();
                    }
                    if ( event.getID () == MouseEvent.MOUSE_RELEASED ) {
                        adapter.onMouseReleased();
                    }
                    if ( event.getID () == MouseEvent.MOUSE_ENTERED ) {
                        adapter.onMouseEnter();
                    }
                    if ( event.getID () == MouseEvent.MOUSE_EXITED ) {
                        adapter.onMouseExit();
                    }
                    // No mouse stay, I'm too stupid for that
                }
            }
        };
        Toolkit.getDefaultToolkit ().addAWTEventListener ( listener, AWTEvent.MOUSE_EVENT_MASK );

    }

    public static void init(JFrame frame) {
        GlobalInput.frame = frame;
    }

    @SuppressWarnings("all")
    public static GlobalInput getInstance() {
        if (instance == null) {
            instance = new GlobalInput();
        }
        return instance;
    }

    public static void addMouseActionListener(MouseActionAdapter adapter) {
        mouseActions.add(adapter);
    }
}
