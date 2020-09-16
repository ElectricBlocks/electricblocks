package edu.uidaho.electricblocks.electric;

/**
 * Volt class is used to hold volt information and allow it to be easily converted between units.
 */
public class Volt {

    private double volts;

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

    public void setVolts(double volts) {
        this.volts = volts;
    }

    public void setKiloVolts(double kiloVolts) {
        this.volts = kiloVolts * 1000d;
    }

    public void setMegaVolts(double megaVolts) {
        this.volts = megaVolts * 1000000d;
    }

}
