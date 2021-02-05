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

    /**
     * Gets the number of watts in this instance
     * @return The number of watts
     */
    public double getWatts() {
        return watts;
    }

    /**
     * Gets the number of kilowatts in this instance. This just divides the internal value by 1000 to do the conversion.
     * @return The number of kilowatts
     */
    public double getKiloWatts() {
        return watts / 1000d;
    }

    /**
     * Gets the number of megawatts in this instance. This just divides the internal value by 1000000 to do the
     * conversion.
     * @return The number of megawatts
     */
    public double getMegaWatts() {
        return watts / 1000000d;
    }

}
