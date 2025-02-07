package com.sarrygeez.Core.Actions;


import com.sarrygeez.Application;
import com.sarrygeez.Core.Rendering.Camera;
import com.sarrygeez.Data.Vector2;

public class Goto implements Action{

    private final Camera camera;
    private final Vector2 newPos;

    public Goto(Camera camera, Vector2 newPos) {
        this.camera = camera;
        this.newPos = newPos;
    }

    @Override
    public void execute() {
        camera.setPosition(Application.toCartesianCoordinate(newPos));
    }

    @Override
    public String name() {
        return String.format("Goto {%d : %d}", newPos.getX_int(), newPos.getY_int());
    }
}
