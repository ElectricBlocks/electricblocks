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

    public double getVolts() {
        return volts;
    }

    public double getKiloVolts() {
        return volts / 1000d;
    }

    public double getMegaVolts() {
        return volts / 1000000d;
    }

}
