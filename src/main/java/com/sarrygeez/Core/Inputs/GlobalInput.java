package com.sarrygeez.Core.Inputs;

import com.sarrygeez.Data.KeyActionAdapter;
import com.sarrygeez.Data.MouseActionAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class GlobalInput {

    private static GlobalInput instance;
    private static JFrame frame;

    private static final ArrayList<MouseActionAdapter> mouseActions = new ArrayList<>();
    private static final ArrayList<KeyActionAdapter> keyActions = new ArrayList<>();

    private static final HashMap<Integer, Boolean> modifierKeys;
    static {
        // initialize hashmap keys
        modifierKeys = new HashMap<>();
        modifierKeys.put(KeyEvent.VK_CONTROL, false);
        modifierKeys.put(KeyEvent.VK_SHIFT, false);
        modifierKeys.put(KeyEvent.VK_ALT, false);
    }

    private GlobalInput() {
        // Global mouse listener
        final AWTEventListener mouseListener = event -> {

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

        // Global Key modifiers listener
        final AWTEventListener keyListener = event -> {

            if ( event.getID () == KeyEvent.KEY_PRESSED ) {
                flipValue(((KeyEvent) event).getKeyCode(), true);
            }
            if ( event.getID () == KeyEvent.KEY_RELEASED ) {
                flipValue(((KeyEvent) event).getKeyCode(), false);
            }

            for(KeyActionAdapter adapter : keyActions) {
                if (isActivated(KeyEvent.VK_ALT))
                    adapter.onAltMod();

                if (isActivated(KeyEvent.VK_SHIFT))
                    adapter.onShiftMod();

                if (isActivated(KeyEvent.VK_CONTROL))
                    adapter.onControlMod();
            }
        };

        Toolkit.getDefaultToolkit ().addAWTEventListener ( mouseListener, AWTEvent.MOUSE_EVENT_MASK );
        Toolkit.getDefaultToolkit ().addAWTEventListener ( keyListener, AWTEvent.KEY_EVENT_MASK );
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

    @SuppressWarnings("unused")
    public static void addMouseActionListener(MouseActionAdapter adapter) {
        mouseActions.add(adapter);
    }

    @SuppressWarnings("unused")
    public static void addKeyListener(KeyActionAdapter adapter) {
        keyActions.add(adapter);
    }

    public static boolean isActivated(int keycode) {
        return modifierKeys.get(keycode);
    }

    private void flipValue(int keycode, boolean intoTrue) {
        if (modifierKeys.containsKey(keycode) && modifierKeys.get(keycode) != intoTrue) {
            modifierKeys.put(keycode, intoTrue);
        }
    }
}
