package edu.uidaho.electricblocks.simulation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.uidaho.electricblocks.ElectricBlocksConfig;
import edu.uidaho.electricblocks.ElectricBlocksMod;
import edu.uidaho.electricblocks.utils.PlayerUtils;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Singleton class that handles simulation tasks. This class contains a list of
 * SimulationNetworks. Each SimulationNetwork is responsible for converting
 * itself into a format that can be understood by the simulator. This class
 * handles the actual communication between the simulator and the
 * SimulationNetworks. When the simulation is complete and results are received,
 * the results are passed off to the SimulationNetwork to be parsed and then to
 * update each element in the Simulation.
 */
public class SimulationHandler {

    private static SimulationHandler instance = null;
    private static final String addr = ElectricBlocksConfig.getHostURL();
    private List<SimulationNetwork> networkList = Collections.synchronizedList(new ArrayList<>());
    private Thread asyncSimThread;

    private SimulationHandler() {
        asyncSimThread = new Thread() {
            /**
             * Simulations are ran asynchronously in a separate thread, but are all
             * calculated in order. This is done so that subsequent changes to the same
             * block are always performed in the order that the player modifies them in
             * game.
             */
            public void run() {
                ElectricBlocksMod.LOGGER.info("Starting simulation handler thread!");
                while (true) {
                    if (networkList.size() > 0 && networkList.get(0).isReady()) {
                        SimulationNetwork sim = networkList.remove(0); // Pop network from beginning of list
                        JsonObject result = simRequest(sim);
                        if (result == null) {
                            // Notifying player about failed connection is done in simRequest call
                            sim.zeroSimResults();
                        } else if (result.get("status").getAsString().equals("SIM_RESULT")) {
                            sim.handleSimulationResults(result);
                        } else if (result.get("status").getAsString().equals("CONV_ERROR")) {
                            sim.zeroSimResults();
                            PlayerUtils.warn(sim.getPlayer(), "command.electricblocks.requestsimulation.warn_conv");
                        } else {
                            sim.zeroSimResults();
                        }
                    }
                }
            }
        };
        asyncSimThread.start();
    }

    public static SimulationHandler instance() {
        if (instance == null) {
            instance = new SimulationHandler();
        }
        return instance;
    }

    public boolean sendKeepAlive() throws Exception {
        String keepAlive = "{\"status\": \"KEEP_ALIVE\"}";
        String response = sendPost(keepAlive);
        ElectricBlocksMod.LOGGER.info("Sending keep alive.");
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        if (jsonObject.get("status").getAsString().equals("KEEP_ALIVE")) {
            ElectricBlocksMod.LOGGER.info("Keep Alive successful!");
            return true;
        }
        ElectricBlocksMod.LOGGER.fatal("Invalid or malformed keep alive request! Dumping response:");
        ElectricBlocksMod.LOGGER.fatal(jsonObject.toString());
        return false;
    }

    private JsonObject simRequest(SimulationNetwork simNetwork) {
        JsonObject requestJson = new JsonObject();
        requestJson.addProperty("status", "SIM_REQUEST");
        requestJson.addProperty("3phase", false); // TODO make 3phase system work
        JsonObject elements = new JsonObject();
        for (SimulationTileEntity sim : simNetwork.getSimulationList()) {
            JsonObject simJs = sim.toJson();
            for (Map.Entry<String, JsonElement> entry : simJs.entrySet()) {
                elements.add(entry.getKey(), entry.getValue());
            }
        }
        for (SimulationConnection simConn : simNetwork.getSimulationConnections()) {
            elements.add(simConn.getSimId().toString(), simConn.toJson());
        }
        requestJson.add("elements", elements);
        if (ElectricBlocksConfig.getLogJSONRequests()) {
            ElectricBlocksMod.LOGGER.debug(requestJson.toString());
        }
        String responseString;
        try {
            responseString = sendPost(requestJson.toString());
        } catch (Exception e) {
            if (simNetwork.hasPlayer()) {
                PlayerUtils.error(simNetwork.getPlayer(), "command.electricblocks.requestsimulation.error_conn");
            }
            ElectricBlocksMod.LOGGER.fatal("ElectricBlocks experienced a connection issue with EBPP:");
            e.printStackTrace();
            ElectricBlocksMod.LOGGER.fatal("ElectricBlocks experienced a connection issue with EBPP. See the above error for more info.");
            return null;
        }

        JsonObject responseJson = new JsonParser().parse(responseString).getAsJsonObject();
        if (ElectricBlocksConfig.getLogJSONRequests()) {
            ElectricBlocksMod.LOGGER.debug(responseJson.toString());
        }
        return responseJson;
    }

    private String sendPost(String body) throws Exception {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(addr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(body);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line).append("\n");
            }
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }

    public void newSimulationNetwork(SimulationTileEntity ste) {
        newSimulationNetwork(ste, null);
    }

    public void newSimulationNetwork(SimulationTileEntity ste, PlayerEntity player) {
        SimulationNetwork simulationNetwork = new SimulationNetwork(ste);
        simulationNetwork.setPlayer(player);
        networkList.add(simulationNetwork);
    }
}
