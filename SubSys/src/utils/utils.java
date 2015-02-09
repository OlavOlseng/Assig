package utils;

/**
 * Created by Olav on 29/01/2015.
 */

public class Utils {

    public static float vecLength(float x, float y) {
        return (float) Math.sqrt(Math.pow(x, 2) + java.lang.Math.pow(y, 2));
    }

    public static float vecDot(float x1, float y1, float x2, float y2) {
        return x1* x2 + y1 * y2;
    }
}
