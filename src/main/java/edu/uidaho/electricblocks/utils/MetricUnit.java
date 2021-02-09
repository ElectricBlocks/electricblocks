package edu.uidaho.electricblocks.utils;

/**
 * MetricUnit class is used to hold SI unit information and allow it to be easily converted between units.
 * This class is immutable and so a new instance needs to be created for it to be changed.
 */
public class MetricUnit {

    public enum MetricPrefix {
        PICO, NANO, MICRO, MILLI, CENTI, DECI, STANDARD, DECA, HECTO, KILO, MEGA, GIGA, TERRA
    }

    public enum BaseUnit {
        VOLT, WATT, AMP, UNKNOWN
    }

    private final double value;
    private final BaseUnit baseUnit;

    /**
     * Initialize a metric unit with out a prefix which means the value is in standard units.
     * @param value The value of the metric unit in standard units
     */
    public MetricUnit(double value) {
        this(value, MetricPrefix.STANDARD, BaseUnit.UNKNOWN);
    }

    /**
     * Initializes a metric unit with a prefix, but without a specified unit type
     * @param value The value of the metric unit in the specified prefixes units
     * @param prefix The prefix for multiples of this unit
     */
    public MetricUnit(double value, MetricPrefix prefix) {
        this(value, prefix, BaseUnit.UNKNOWN);
    }

    /**
     * Initializes a metric unit with a standard prefix and with a specified unit
     * @param value The value of the metric unit in standard units
     * @param baseUnit The underlying base units that this class represents
     */
    public MetricUnit(double value, BaseUnit baseUnit) {
        this(value, MetricPrefix.STANDARD, baseUnit);
    }

    /**
     * Initialize a metric unit with a specific prefix. For example, if another program returns a value in Mega SI units
     * then the appropriate MetricPrefix will convert the value for you
     * @param value The value of the metric unit
     * @param prefix The metric prefix that the metric unit is represented with
     * @param baseUnit The underlying units that this class represents
     */
    public MetricUnit(double value, MetricPrefix prefix, BaseUnit baseUnit) {
        this.baseUnit = baseUnit;
        switch (prefix) {
            case PICO:
                this.value = value / 1000000000000d;
                break;
            case NANO:
                this.value = value / 1000000000d;
                break;
            case MICRO:
                this.value = value / 1000000d;
                break;
            case MILLI:
                this.value = value / 1000d;
                break;
            case CENTI:
                this.value = value / 100d;
                break;
            case DECI:
                this.value = value / 10d;
                break;
            case DECA:
                this.value = value * 10d;
                break;
            case HECTO:
                this.value = value * 100d;
                break;
            case KILO:
                this.value = value * 1000d;
                break;
            case MEGA:
                this.value = value * 1000000d;
                break;
            case GIGA:
                this.value = value * 1000000000d;
                break;
            case TERRA:
                this.value = value * 1000000000000d;
                break;
            default: // Covers standard as well
                this.value = value;
                break;
        }
    }

    /**
     * Gets the metric value in pico units
     * @return The metric value in pico units
     */
    public double getPico() {
        return this.value * 1000000000000d;
    }

    /**
     * Gets the metric value in nano units
     * @return The metric value in nano units
     */
    public double getNano() {
        return this.value * 1000000000d;
    }

    /**
     * Gets the metric value in micro units
     * @return The metric value in micro units
     */
    public double getMicro() {
        return this.value * 1000000d;
    }

    /**
     * Gets the metric value in milli units
     * @return The metric value in milli units
     */
    public double getMilli() {
        return this.value * 1000d;
    }

    /**
     * Gets the metric value in centi units
     * @return The metric value in centi units
     */
    public double getCenti() {
        return this.value * 100d;
    }

    /**
     * Gets the metric value in deci units
     * @return The metric value in deci units
     */
    public double getDeci() {
        return this.value * 10d;
    }

    /**
     * Gets the metric value in standard / non-prefixed units
     * @return The metric value in standard / non-prefixed units
     */
    public double get() {
        return this.value;
    }

    /**
     * Gets the metric value in deca units
     * @return The metric value in deca units
     */
    public double getDeca() {
        return this.value / 10d;
    }

    /**
     * Gets the metric value in hecto units
     * @return The metric value in hecto units
     */
    public double getHecto() {
        return this.value / 100d;
    }

    /**
     * Gets the metric value in kilo units
     * @return The metric value in kilo units
     */
    public double getKilo() {
        return this.value / 1000d;
    }

    /**
     * Gets the metric value in mega units
     * @return The metric value in mega units
     */
    public double getMega() {
        return this.value / 1000000d;
    }

    /**
     * Gets the metric value in giga units
     * @return The metric value in giga units
     */
    public double getGiga() {
        return this.value / 1000000000d;
    }

    /**
     * Gets the metric value in terra units
     * @return The metric value in terra units
     */
    public double getTerra() {
        return this.value / 1000000000000d;
    }

    /**
     * Gets the base units that this instance represents.
     * @return The base units that this instance represents. May return BaseUnit.UNKNOWN if this was never set.
     */
    public BaseUnit getBaseUnits() {
        return this.baseUnit;
    }

    /**
     * Checks whether or not the base units have been set for this instance.
     * @return Whether or not the base units have been set for this instance.
     */
    public boolean isBaseUnitsKnown() {
        return this.baseUnit != BaseUnit.UNKNOWN;
    }

}