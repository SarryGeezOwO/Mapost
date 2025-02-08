package com.sarrygeez.Core.Actions;

import com.sarrygeez.Application;
import com.sarrygeez.Core.Rendering.GridMapContext;
import com.sarrygeez.Posts.TextPost;
import com.sarrygeez.Data.Vector2;

public class SpawnPost implements Action{

    private final Vector2 pos;
    private final String message;

    public SpawnPost(Vector2 pos, String msg) {
        this.pos = pos;
        this.message = msg;
    }

    @Override
    public void execute() {
        // something idk, please kill me
        // Fuck Software in general bro
        TextPost post = new TextPost("Sarrie", "...", message, "");
        post.transform.setPosition(Vector2.scale(pos, GridMapContext.CELL_SIZE));
        Application.GRID_MAP_CONTEXT.objects.add(post);
    }

    @Override
    public String name() {
        return String.format("Spawn Post {%d : %d}", pos.getX_int(), pos.getY_int());
    }
}
