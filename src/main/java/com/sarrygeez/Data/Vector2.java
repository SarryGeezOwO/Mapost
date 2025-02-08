package com.sarrygeez.Data;

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

    public int getX_int() {
        return (int)x;
    }

    public int getY_int() {
        return (int)y;
    }

    public static Vector2 mult(Vector2 a, Vector2 b) {
        return new Vector2(
                a.x * b.x,
                a.y * b.y
        );
    }

    public static Vector2 scale(Vector2 target, float scale) {
        return new Vector2(
                target.x * scale,
                target.y * scale
        );
    }

    public static String formatStr(Vector2 vector) {
        return String.format("(%.2f, %.2f)", vector.x, vector.y);
    }

    public static float dotProduct(Vector2 vec1, Vector2 vec2) {
        return vec1.x * vec2.x + vec1.y * vec2.y;
    }
}
