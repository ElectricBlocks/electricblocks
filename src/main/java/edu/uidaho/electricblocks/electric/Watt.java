package edu.uidaho.electricblocks.electric;

/**
 * Class representing a watt unit for easy conversion.
 * This class is immutable. If you want to change voltage, you must use a new instance.
 */
public class Watt {

    private final double watts;

    public Watt(double watts) {
        this.watts = watts;
    }

    public double getWatts() {
        return watts;
    }

    public double getKiloWatts() {
        return watts / 1000d;
    }

    public double getMegaWatts() {
        return watts / 1000000d;
    }

}
