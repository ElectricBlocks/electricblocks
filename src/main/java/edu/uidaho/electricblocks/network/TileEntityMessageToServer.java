package edu.uidaho.electricblocks.network;

import edu.uidaho.electricblocks.ElectricBlocksMod;
import edu.uidaho.electricblocks.simulation.SimulationTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nonnull;

public class TileEntityMessageToServer {

    /**
     * Official Specification for TileEntityMessageToServer is the following:
     * ======================================================================
     * 1. int x; // The x coordinate of the tile entity
     * 2. int y; // The y coordinate of the tile entity
     * 3. int z; // The z coordinate of the tile entity
     * 4. boolean inService; // Is the tile entity in service?
     * 5. int numInputs; // The number of inputs associated with a tile entity.
     * 6. double inputs[0]
     * 7. double inputs[1]
     * ...
     *    double inputs[numInputs] // The final input
     */

    private int x;
    private int y;
    private int z;
    private boolean inService;
    private int numInputs;
    private double[] inputs;
    private boolean isValid = false;

    private TileEntityMessageToServer() {} // Only instantiable like this inside class

    public TileEntityMessageToServer(@Nonnull SimulationTileEntity ste, @Nonnull PlayerEntity player) {
        x = ste.getPos().getX();
        y = ste.getPos().getY();
        z = ste.getPos().getZ();
        inService = ste.isInService();
        numInputs = ste.getNumInputs();
        inputs = new double[numInputs];
        ste.fillPacketBuffer(inputs);
    }

    /**
     * Called by the network code once it has received the messsage bytes over the network.
     * Used to read the PacketBuffer contents into your member variables
     * @param buf The packet that was received
     * @return The parsed TileEntityMessageToServer
     */
    public static TileEntityMessageToServer decode(PacketBuffer buf) {
        TileEntityMessageToServer msg = new TileEntityMessageToServer();
        try {
            msg.x = buf.readInt();
            msg.y = buf.readInt();
            msg.z = buf.readInt();
            msg.inService = buf.readBoolean();
            msg.numInputs = buf.readInt();
            msg.inputs = new double[msg.numInputs];
            for (int i = 0; i < msg.numInputs; i++) {
                msg.inputs[i] = buf.readDouble();
            }
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            ElectricBlocksMod.LOGGER.warn("Server received a malformed message! " + e);
            return msg;
        }
        msg.isValid = true;
        return msg;
    }

    /**
     * Called by the network code and is used to write the contents of message member variables into
     * the packet buffer so it can be transmitted over the network
     * @param buf The packet buffer that will hold the tile entity info
     */
    public void encode(PacketBuffer buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeBoolean(inService);
        buf.writeInt(numInputs);
        for (int i = 0; i < numInputs; i++) {
            buf.writeDouble(inputs[i]);
        }
    }

    /**
     * @return The x coordinate for the STE that was updated
     */
    public int getX() {
        return x;
    }

    /**
     * @return The y coordinate for the STE that was updated
     */
    public int getY() {
        return y;
    }

    /**
     * @return The z coordinate for the STE that was updated
     */
    public int getZ() {
        return z;
    }

    /**
     * @return Whether or not the updated STE is in service
     */
    public boolean isInService() {
        return inService;
    }

    /**
     * @return The number of numerical inputs for the STE that was updated
     */
    public int getNumInputs() {
        return numInputs;
    }

    /**
     * @return The array of numerical inputs for the STE that was updated
     */
    public double[] getInputs() {
        return inputs;
    }

    /**
     * @return Whether or not this packet is valid or should result in an error
     */
    public boolean isValid() {
        return isValid;
    }

}
