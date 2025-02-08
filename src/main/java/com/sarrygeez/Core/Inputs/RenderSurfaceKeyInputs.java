package com.sarrygeez.Core.Inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderSurfaceKeyInputs implements KeyListener {

    // keycode, Status
    private final HashMap<Integer, Boolean> modifierKeys = new HashMap<>();

    public RenderSurfaceKeyInputs() {
        // initialize hashmap keys
        modifierKeys.put(KeyEvent.VK_CONTROL, false);
        modifierKeys.put(KeyEvent.VK_SHIFT, false);
        modifierKeys.put(KeyEvent.VK_ALT, false);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        flipValue(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        flipValue(e.getKeyCode(), false);
    }

    private void flipValue(int keycode, boolean intoTrue) {
        if (modifierKeys.containsKey(keycode) && modifierKeys.get(keycode) != intoTrue) {
            modifierKeys.put(keycode, intoTrue);
        }
    }

    public void resetModifiers() {
        modifierKeys.replaceAll((key, val) -> false);
    }

    public boolean isActivated(int keycode) {
        return modifierKeys.get(keycode);
    }

    public List<Integer> getActivatedModifier() {
        return modifierKeys.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .toList();
    }
}
