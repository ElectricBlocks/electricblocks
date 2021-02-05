package edu.uidaho.electricblocks.electric;

/**
 * Volt class is used to hold volt information and allow it to be easily converted between units.
 * This class is immutable and so a new instance needs to be created for it to be changed.
 */
public class Volt {

    private final double volts;

    public Volt(double volts) {
        this.volts = volts;
    }

    /**
     * Gets the number of volts in this instance.
     * @return The number of volts
     */
    public double getVolts() {
        return volts;
    }

    /**
     * Gets the number of kilovolts in this instance. This just divides the internal value by 1000 to do the conversion.
     * @return The number of kilovolts
     */
    public double getKiloVolts() {
        return volts / 1000d;
    }

    /**
     * Gets the number of megavolts in this instance. This just divides the internal value by 1000000 to do the
     * conversion.
     * @return The number of megavolts
     */
    public double getMegaVolts() {
        return volts / 1000000d;
    }

}
