package edu.uidaho.electricblocks.simulation;

import com.google.gson.JsonElement;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;

/**
 * A simulation property represents an input for a specific electrical element in the load flow simulation. These are
 * usually double values but can also be boolean or string values. This class contains additional information used by
 * the GUI such as the name of the property and the units. This class is also used for networking code in order to
 * encode and decode packets for updating STEs.
 */
public class SimulationProperty {

    /**
     * The data type that this simulation property represents. I tried using generics, but that complicated fast so I
     * decided to just explicitly list out the acceptable types.
     */
    public enum PropertyType {
        STRING, BOOL, DOUBLE
    }

    private final String label; // The name of the input
    private final String units; // The units that the input is in
    private Object data; // The data itself
    private PropertyType propertyType; // Type of the data for casting
    private final boolean sendInJSON; // False for properties that are editable in menu, but require special handling for json

    /**
     * Constructor for a new simulation property. sendInJSON defaults to true when using this constructor. Used for most
     * numeric inputs.
     * @param label The name of the input
     * @param units The units that the input is in
     * @param defaultValue The default value that is assigned for this property on instantiation
     */
    public SimulationProperty(String label, String units, Object defaultValue) {
        this(label, units, defaultValue, true);
    }

    /**
     * Constructor for a new simulation property, but sendInJSON can be directly specified.
     * @param label The name of the input
     * @param units The units that the input is in
     * @param defaultValue The default value that is assigned for this property on instantiation
     * @param sendInJSON Whether or not this property value should be included in the JSON representation of the element
     *                   that contains this property
     */
    public SimulationProperty(String label, String units, Object defaultValue, boolean sendInJSON) {
        this.label = label;
        this.units = units;
        this.data = defaultValue;
        this.sendInJSON = sendInJSON;
        if (defaultValue instanceof Boolean) {
            propertyType = PropertyType.BOOL;
        } else if (defaultValue instanceof String) {
            propertyType = PropertyType.STRING;
        } else if (defaultValue instanceof Double) {
            propertyType = PropertyType.DOUBLE;
        } else {
            throw new UnsupportedOperationException("defaultValue is not an instance of a supported property type");
        }
    }

    /**
     * Fills an NBT tag with this property. Used by SimulationTileEntity class to update NBT tags in memory
     * @param id The id of the property
     * @param compound The tag that is being written to
     */
    public void fillNBT(String id, @Nonnull CompoundNBT compound) {
        switch (propertyType) {
            case STRING:
                compound.putString(id, getString());
                return;
            case BOOL:
                compound.putBoolean(id, getBoolean());
                return;
            case DOUBLE:
                compound.putDouble(id, getDouble());
                return;
        }
    }

    /**
     * Reads this property from the NBT tag stored in the SimulationTileEntity.
     * @param id The id of the property
     * @param compound The tag that is being read from
     */
    public void readNBT(String id, @Nonnull CompoundNBT compound) {
        switch (propertyType) {
            case STRING:
                data = compound.getString(id);
                return;
            case BOOL:
                data = compound.getBoolean(id);
                return;
            case DOUBLE:
                data = compound.getDouble(id);
                return;
        }
    }

    /**
     * Reads that value of a json element and stores it in the data field of this property. Used to update data when a
     * simulation result has been received.
     * @param jsonElement The JsonElement associated with this property
     */
    public void readJSON(JsonElement jsonElement) {
        switch (propertyType) {
            case STRING:
                data = jsonElement.getAsString();
                return;
            case BOOL:
                data = jsonElement.getAsBoolean();
                return;
            case DOUBLE:
                data = jsonElement.getAsDouble();
                return;
        }
    }

    /**
     * @return The simulation property cast to String
     */
    public String getString() {
        checkType(String.class);
        return (String) data;
    }

    /**
     * @return The simulation property cast to Boolean
     */
    public Boolean getBoolean() {
        checkType(Boolean.class);
        return (Boolean) data;
    }

    /**
     * @return The simulation property cast to Double
     */
    public Double getDouble() {
        checkType(Double.class);
        return (Double) data;
    }

    /**
     * Sets the data stored in this simulation property
     * @param data The data to be stored
     */
    public void set(Object data) {
        this.data = data;
    }

    /**
     * @return The label associated with this simulation property. This is the name of the property and could be
     * something like Max Power or Voltage.
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return The units associated with this simulation property. The units are usually the default for PandaPower and
     * could be something like V (volts), A (amps), kW (kilowatts), etc
     */
    public String getUnits() {
        return units;
    }

    /**
     * @return The type of the data stored in this simulation property from the subset defined in the PropertyType enum
     */
    public PropertyType getPropertyType() {
        return propertyType;
    }

    /**
     * Checks whether or not the data represented by this class is an instance of the input class. Throws a
     * ClassCastException if the data is not an instance of the passed in class. This is an unchecked runtime exception
     * which is not recoverable from and indicates an issue with logic introduced by the programmer.
     * TODO: This exception model is probably not ideal and may want to be investigated in the future
     * @param clazz
     */
    public void checkType(Class<?> clazz) {
        if (!clazz.isInstance(data)) {
            throw new ClassCastException(String.format("Tried to get property %s with property type %s as %s!", label, propertyType.toString(), clazz.getName()));
        }
    }

    /**
     * @return Whether or not this simulation property should be included in the JSON representation of the electrical
     * element that owns this property.
     */
    public boolean shouldSendJSON() {
        return sendInJSON;
    }

    /**
     * Creates a clone of this simulation property. Usually used for initializing defaults.
     * @return A copy of this simulation property.
     */
    public SimulationProperty clone() {
        return new SimulationProperty(label, units, data, sendInJSON);
    }
}
