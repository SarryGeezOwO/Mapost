package com.sarrygeez.Components;

import com.sarrygeez.Application;
import com.sarrygeez.Core.Inputs.GlobalInput;
import com.sarrygeez.Core.Inputs.MouseActionAdapter;
import com.sarrygeez.Core.Rendering.Camera;
import com.sarrygeez.Core.Rendering.RenderSurface;
import com.sarrygeez.Data.Transform;
import com.sarrygeez.Data.Vector2;
import com.sarrygeez.Debug.Debug;
import com.sarrygeez.Debug.LogLevel;
import com.sarrygeez.Tools.AppGraphics;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class LocationBar extends RectComponent {

    public boolean hasInitialized = false;
    private final Camera camera;

    private final int barWidth;
    private final int barHeight;

    public LocationBar(Camera camera) {
        this.camera = camera;
        barWidth = 200;
        barHeight = 30;
    }
    public void init() {
        hasInitialized = true;
        transform.setScale(new Vector2(Transform.translateSizeToScale(200), Transform.translateSizeToScale(30)));

        GlobalInput.addMouseActionListener(new MouseActionAdapter() {
            @Override
            public void onMouseClick() {
                if (!positionInsideBbox(Camera.MOUSE_CAM_POS)) {
                    return;
                }
                StringSelection selection = new StringSelection(Vector2.formatStr(camera.getWorldPosition()));
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
                Debug.log("Successfully copied current location!", LogLevel.SUCCESS);
            }
        });
    }

    @Override
    public void update() {
        transform.setPosition(new Vector2(
                (camera.position.getX_int() +  Application.WINDOW_WIDTH /2) - (barWidth / 2),
                (camera.position.getY_int() +  Application.WINDOW_HEIGHT) - (barHeight + 10)
        ));
        super.update();
    }

    @Override
    public void draw(Graphics2D g2d, Camera camera) {
        int width = RenderSurface.WIDTH;
        int height = RenderSurface.HEIGHT;
        AppGraphics.drawRect(camera, new Vector2(width/2 -100, height-40), new Vector2(width/2 + 100, height-10),
                10, 1, true, background, borderColor
        );
        AppGraphics.drawLine(camera, new Vector2(width/2, height - 32), new Vector2(width/2, height - 18), 2, Color.BLACK);
        AppGraphics.drawTextExt(camera, new Vector2(width/2, height - 50), "x  LOCATION  y", Color.WHITE, 3, 3);

        // X position
        AppGraphics.drawTextExt(camera, new Vector2(width/2 - 50, height - 25),
                String.format("%.2f", camera.getWorldPosition().x),
                Color.BLACK, 3, 3
        );
        // Y position
        AppGraphics.drawTextExt(camera, new Vector2(width/2 + 50, height - 25),
                String.format("%.2f", camera.getWorldPosition().y),
                Color.BLACK, 3, 3
        );
    }
}
