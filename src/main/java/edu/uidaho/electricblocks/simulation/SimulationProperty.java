package edu.uidaho.electricblocks.simulation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.UUID;

public class SimulationProperty {

    private final String label;
    private final String units;
    private Object data;
    private PropertyType propertyType;

    public SimulationProperty(String label, String units, Object defaultValue) {
        this.label = label;
        this.units = units;
        this.data = defaultValue;
    }

    public void fillNBT(String id, @Nonnull CompoundNBT compound) {
        switch (propertyType) {
            case STRING:
                compound.putString(id, getString());
                return;
            case UUID:
                compound.putUniqueId(id, getUUID());
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
            case UUID:
                data = compound.getUniqueId(id);
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
            case UUID:
                data = UUID.fromString(jsonElement.getAsString());
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

    public UUID getUUID() {
        checkType(UUID.class);
        return (UUID) data;
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

    public enum PropertyType {
        STRING, UUID, BOOL, DOUBLE
    }
}
