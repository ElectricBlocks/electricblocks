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

import edu.uidaho.electricblocks.ElectricBlocksMod;

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
    private static final String addr = "http://127.0.0.1:1127/api"; // TODO Read from config file
    private List<SimulationNetwork> networkList = Collections.synchronizedList(new ArrayList<>());
    private Thread asyncSimThread;

    private SimulationHandler() {
        asyncSimThread = new Thread() {
            /**
            * Simulations are ran asynchronously in a separate thread, but are all calculated in order.
            * This is done so that subsequent changes to the same block are always performed in the
            * order that the player modifies them in game.
            */
            public void run() {
                ElectricBlocksMod.LOGGER.info("Starting simulation handler thread!");
                while (true) {
                    if (networkList.size() > 0 && networkList.get(0).isReady()) {
                        SimulationNetwork sim = networkList.remove(0); // Pop network from beginning of list
                        JsonObject result = simRequest(sim);
                        if (result.get("status").getAsString().equals("SIM_RESULT")) {
                            sim.handleSimulationResults(result);
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

    public void sendKeepAlive() {
        String keepAlive = "{\"status\": \"KEEP_ALIVE\"}";
        String response = sendPost(keepAlive);
        ElectricBlocksMod.LOGGER.info("Sending keep alive.");
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        if (jsonObject.get("status").getAsString().equals("KEEP_ALIVE")) {
            ElectricBlocksMod.LOGGER.info("Keep Alive successful!");
        } else {
            ElectricBlocksMod.LOGGER.fatal("Could not contact EBPP server!");
            throw new RuntimeException("Could not contact EBPP server. Fatal error!");
        }
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
        ElectricBlocksMod.LOGGER.debug(requestJson.toString());
        String responseString = sendPost(requestJson.toString());
        JsonObject responseJson = new JsonParser().parse(responseString).getAsJsonObject();
        ElectricBlocksMod.LOGGER.debug(responseJson.toString());
        return responseJson;
    }

    private String sendPost(String body) {
        // TODO Gracefully handle post failure
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
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
                result += line + "\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        return result;
    }

    public void newSimulationNetwork(SimulationTileEntity ste) {
        SimulationNetwork simulationNetwork = new SimulationNetwork(ste);
        networkList.add(simulationNetwork);
    }
}
