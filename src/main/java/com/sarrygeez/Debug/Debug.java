package com.sarrygeez.Debug;

import java.awt.*;

public class Debug {

    private Debug() {}

    public static void log(String msg, LogLevel level) {
        // Date and time?
        System.out.print("[" + level.name() + "] -- ");
        System.out.println(msg);
    }

    private static Color getLevelColor(LogLevel level) {
        return switch (level) {
            case WARNING ->     Color.YELLOW;
            case OK ->          Color.decode("#64FF21");
            case SUCCESS ->     Color.GREEN;
            case ERROR ->       Color.RED;
            case DEBUG ->       Color.BLACK;
        };
    }

}
