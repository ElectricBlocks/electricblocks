package edu.uidaho.electricblocks.utils;

public class MathUtils {

    /**
     * Clamps a number between a minimum and a maximum value
     * @param input The number to clamp
     * @param min The minimum value that the input can be
     * @param max The maximum value that the input can be
     * @return The input if it is within the range, otherwise the corresponding min/max value.
     */
    public static double clamp(double input, double min, double max) {
        if (input < min) {
            return min;
        } else if (input > max) {
            return max;
        }

        return input;
    }
    
}
