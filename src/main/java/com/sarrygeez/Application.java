package com.sarrygeez;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class Application extends JFrame {

    protected static ArrayList<FlatSVGIcon> icons = new ArrayList<>();
    public static final String WINDOW_TITLE = "Mapost";
    public static final String APP_VERSION = "1.0.0";
    public static int WINDOW_WIDTH = 1400;
    public static int WINDOW_HEIGHT = 900;

    public static final GridMapContext GRID_MAP_CONTEXT = new GridMapContext();

    public Application() {
        init();
    }

    private void init() {
        // Initialize JComponents
        setTitle(WINDOW_TITLE + " v" + APP_VERSION);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setMinimumSize(new Dimension(700, 500));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        ContentPane cp = new ContentPane();
        setContentPane(cp);

        setIconImage(AppTools.getSVG("AppIMG.svg").getImage());
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                WINDOW_WIDTH = cp.getWidth();
                WINDOW_HEIGHT = cp.getHeight();
                cp.getCamera().updateSize();
            }
        });

        RectComp center = new RectComp(200, 50, Color.WHITE, Color.BLACK);
        center.transform.scale = new Vector2(10f, 5f);
        center.radius = 10;
        center.borderWidth = 2;
        center.transform.updateBbox();
        GRID_MAP_CONTEXT.objects.add(center);
        GRID_MAP_CONTEXT.objects.add(new RectComp(600, 500, Color.GREEN, Color.BLUE));
        GRID_MAP_CONTEXT.objects.add(new RectComp(-400, -500, Color.YELLOW, Color.PINK));
    }

    public static void main(String[] args) {
        // Default Font
        FlatInterFont.install();
        UIManager.put("defaultFont", new Font(FlatInterFont.FAMILY, Font.PLAIN, 13));

        // FlatLaf properties
        FlatLaf.registerCustomDefaultsSource("FlatLafConfig");

        // Initial Theme
        FlatMacDarkLaf.setup();
        EventQueue.invokeLater(() -> new Application().setVisible(true));
    }

}
