package edu.uidaho.electricblocks.simulation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
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
    private List<SimulationNetwork> networkList = new ArrayList<>();
    

    private SimulationHandler() {
        sendKeepAlive();
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    public static SimulationHandler instance() {
        if (instance == null) {

            instance = new SimulationHandler();
        }
        return instance;
    }

    private void sendKeepAlive() {
        String keepAlive = "{\"status\": \"KEEP_ALIVE\"}";
        String response = sendPost(keepAlive);
        ElectricBlocksMod.LOGGER.info("Sending keep alive.");
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        if (jsonObject.get("status").getAsString().equals("KEEP_ALIVE")) {
            ElectricBlocksMod.LOGGER.info("Keep Alive successful!");
        } else {
            ElectricBlocksMod.LOGGER.fatal("Could not contact EBPP server!");
        }
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
}
