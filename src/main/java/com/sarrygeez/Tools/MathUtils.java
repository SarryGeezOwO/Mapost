package com.sarrygeez.Tools;

import com.sarrygeez.Data.Vector2;

public class MathUtils {

    private MathUtils() {}

    /*
        You might be wondering why I'm using Vectors for position
        and not the built-in Point from javax, simply because
        I'm a fucking idiot. I got too attached from using Vector2
        in unity that I somehow transferred it here lol

        this is probably going to be a bad time for me. Determining
        whether a Vector2 is actually a vector or a position(point)
    */

    /**
     *
     * @param a starting point
     * @param b end point
     * @return a Vector that portrays the direction and magnitude between 2 points
     */
    public static Vector2 getVector(Vector2 a, Vector2 b) {
        float x = b.x - a.x;
        float y = b.y - a.y;
        return new Vector2(x, y);
    }

    /**
     * @param a starting point
     * @param b end point
     * @return the normalized vector (unit vector) from point A to point B
     */
    public static Vector2 getVectorNorm(Vector2 a, Vector2 b) {
        Vector2 vec = getVector(a, b);
        float len = getMagnitude(vec);
        if (len != 0) {
            return new Vector2(vec.x / len, vec.y / len);
        }
        return new Vector2();
    }

    /**
     *
     * @param vector the vector to get the length from
     * @return the magnitude(length) of a given Vector
     */
    public static float getMagnitude(Vector2 vector) {
        return (float) Math.sqrt(Math.pow(vector.x, 2) + Math.pow(vector.y, 2));
    }

    /**
     *
     * @param a Starting point
     * @param b End point
     * @return the distance between the two points
     */
    public static float getLength(Vector2 a, Vector2 b) {
        Vector2 vec = getVector(a, b);
        return getMagnitude(vec);
    }

}
