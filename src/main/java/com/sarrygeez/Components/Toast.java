package com.sarrygeez.Components;

import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.sarrygeez.Application;
import com.sarrygeez.Data.Vector2;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Toast extends JWindow {

    public enum Duration {
        SHORT, MODERATE, LONG
    }

    // Milliseconds
    private static final HashMap<Duration, Integer> timeMap;
    static {
        timeMap = new HashMap<>();
        timeMap.put(Duration.SHORT, 1000);
        timeMap.put(Duration.MODERATE, 1900);
        timeMap.put(Duration.LONG, 3000);
    }

    private final Duration duration;

    public Toast(String message, Vector2 position, Duration duration) {
        this.duration = duration;

        // make the background transparent
        setBackground(new Color(0, 0, 0, 0));


        JPanel p = new JPanel() {
            public void paintComponent(Graphics g)
            {
                Graphics2D g2d = (Graphics2D)g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setFont(new Font(FlatInterFont.FAMILY, Font.PLAIN, 14));

                int wid = g2d.getFontMetrics().stringWidth(message);
                int hei = g2d.getFontMetrics().getHeight();

                // draw the boundary of the toast and fill it
                g2d.setColor(Color.black);
                g2d.fillRoundRect(10, 10, wid + 30, hei + 10, 10, 10);
                g2d.setColor(Color.black);
                g2d.drawRoundRect(10, 10, wid + 30, hei + 10, 10, 10);

                // set the color of text
                g2d.setColor(new Color(255, 255, 255, 240));
                g2d.drawString(message, 25, 30);
                int t = 250;

                // draw the shadow of the toast
                for (int i = 0; i < 4; i++) {
                    t -= 60;
                    g2d.setColor(new Color(0, 0, 0, t));
                    g2d.drawRoundRect(10 - i, 10 - i, wid + 30 + i * 2,
                            hei + 10 + i * 2, 10, 10);
                }
                g2d.dispose();
            }
        };

        add(p);
        Vector2 newPos = new Vector2(
                position.x + Application.WINDOW_LOCATION.x,
                position.y + Application.WINDOW_LOCATION.y
        );

        setLocation(Vector2.toPoint(newPos));
        setSize(2000, 100);
    }

    public void showToast() {
        new Thread(() -> {
            try {
                setOpacity(1);
                setVisible(true);

                // wait for some time
                Thread.sleep(timeMap.get(duration));

                // make the message disappear  slowly
                for (double d = 1.0; d > 0.2; d -= 0.1) {
                    Thread.sleep(50);
                    setOpacity((float)d);
                }

                // set the visibility to false
                setVisible(false);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }

}
