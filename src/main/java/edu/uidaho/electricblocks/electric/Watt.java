package edu.uidaho.electricblocks.electric;

public class Watt {

    private double watts;

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

    public void setWatts(double watts) {
        this.watts = watts;
    }

    public void setKiloWatts(double kiloWatts) {
        this.watts = kiloWatts * 1000d;
    }

    public void setMegaWatts(double megaWatts) {
        this.watts = megaWatts * 1000000d;
    }
}
