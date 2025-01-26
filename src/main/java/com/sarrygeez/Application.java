package com.sarrygeez;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.sarrygeez.Posts.TextPost;

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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        RenderSurface cp = new RenderSurface(); // this stands for ContentPane ok? It's not something devious or whatever
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

        TextPost custom = new TextPost("Aniza", "How to cook", "01-25-2025");
        custom.transform.setPosition(new Vector2(255, 255));

        GRID_MAP_CONTEXT.objects.add(new TextPost("SarryGeez", "Hello, World!", "01-25-2025"));
        GRID_MAP_CONTEXT.objects.add(custom);
    }

    public static Vector2 toCartesianCoordinate(Vector2 base) {
        return new Vector2(
            base.getX_int() - Application.WINDOW_WIDTH/2,
            base.getY_int() - Application.WINDOW_HEIGHT/2
        );
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
