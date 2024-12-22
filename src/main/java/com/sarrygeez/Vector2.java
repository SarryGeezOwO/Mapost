package com.sarrygeez;

@SuppressWarnings("unused")
public class Vector2 {
    public float x;
    public float y;

    public Vector2() {
        x = 0;
        y = 0;
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        String xStr = (x % 1 == 0) ? String.format("%.0f", x) : String.valueOf(x);
        String yStr = (y % 1 == 0) ? String.format("%.0f", y) : String.valueOf(y);
        return "[" + xStr + " : " + yStr + "]";
    }


    public static float dotProduct(Vector2 vec1, Vector2 vec2) {
        return vec1.x * vec2.x + vec1.y * vec2.y;
    }
}
