package com.sarrygeez.Logging;

import java.awt.*;
import java.util.HashMap;

public class Debug {

    public static final String RESET = "\u001B[0m";

    private Debug() {}
    private static final HashMap<LogLevel, String> levelMap;
    static {
        levelMap = new HashMap<>();
        levelMap.put(LogLevel.WARNING,  "\u001B[33m");
        levelMap.put(LogLevel.ERROR,    "\u001B[31m");
        levelMap.put(LogLevel.SUCCESS,  "\u001B[32m");
        levelMap.put(LogLevel.OK,       "\u001B[36m");
        levelMap.put(LogLevel.DEBUG,    "\u001B[90m");
    }

    @SuppressWarnings("unused")
    public static Color getLevelColor(LogLevel level) {
        return switch (level) {
            case WARNING ->     Color.YELLOW;
            case OK ->          Color.decode("#64FF21");
            case SUCCESS ->     Color.GREEN;
            case ERROR ->       Color.RED;
            case DEBUG ->       Color.BLACK;
        };
    }

    public static void log(String msg, LogLevel level) {
        new Thread(() -> {
            String col = levelMap.get(level);

            // Date and time?
            System.out.print(col + "[" + level.name() + "] -- " + RESET);
            System.out.println(msg);
        }).start();
    }

}
