package com.sarrygeez;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;

import static com.sarrygeez.Application.icons;

public class AppTools {
    public static void updateIconColor() {
        for(FlatSVGIcon icon : icons) {
            boolean isDarkLAF = UIManager.getLookAndFeel().getName().toLowerCase().contains("dark");
            icon.setColorFilter(new FlatSVGIcon.ColorFilter(color -> isDarkLAF ? Color.WHITE : Color.BLACK));
        }
    }

    @SuppressWarnings("unused")
    public static void setAppTheme(LookAndFeel laf) {
        try {
            FlatAnimatedLafChange.duration = 250;
            FlatAnimatedLafChange.showSnapshot();
            UIManager.setLookAndFeel(laf);
            FlatLaf.updateUI();
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
            updateIconColor();
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unused")
    public static FlatSVGIcon getSVG(String fileName, int size) {
        FlatSVGIcon icon = new FlatSVGIcon("Icons/" + fileName, size, size);
        icons.add(icon);
        updateIconColor();
        return icon;
    }

    public static FlatSVGIcon getSVG(String fileName) {
        return new FlatSVGIcon("Icons/" + fileName);
    }
}
