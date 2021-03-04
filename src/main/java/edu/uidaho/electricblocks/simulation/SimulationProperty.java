package edu.uidaho.electricblocks.simulation;

import com.google.gson.JsonElement;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;

public class SimulationProperty {

    public enum PropertyType {
        STRING, BOOL, DOUBLE
    }

    private final String label;
    private final String units;
    private Object data;
    private PropertyType propertyType;
    private final boolean sendInJSON; // False for properties that are editable in menu, but require special handling for json

    public SimulationProperty(String label, String units, Object defaultValue) {
        this(label, units, defaultValue, true);
    }

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

    public String getString() {
        checkType(String.class);
        return (String) data;
    }

    public Boolean getBoolean() {
        checkType(Boolean.class);
        return (Boolean) data;
    }

    public Double getDouble() {
        checkType(Double.class);
        return (Double) data;
    }

    public void set(Object data) {
        this.data = data;
    }

    public String getLabel() {
        return label;
    }

    public String getUnits() {
        return units;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void checkType(Class<?> clazz) {
        if (!clazz.isInstance(data)) {
            throw new ClassCastException(String.format("Tried to get property %s with property type %s as %s!", label, propertyType.toString(), clazz.getName()));
        }
    }

    public boolean shouldSendJSON() {
        return sendInJSON;
    }

    public SimulationProperty clone() {
        return new SimulationProperty(label, units, data, sendInJSON);
    }
}
