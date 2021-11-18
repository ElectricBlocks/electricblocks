package edu.uidaho.electricblocks;

import java.text.DecimalFormat;
import java.util.List;

import edu.uidaho.electricblocks.plugins.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import edu.uidaho.electricblocks.lib.Feature;
import net.minecraft.util.NonNullList;

public class EBHWYLA {
    public static final Logger LOG = LogManager.getLogger("electricblocks");
    public static final DecimalFormat FORMAT = new DecimalFormat("#.##");
    private static final List<Feature> features = NonNullList.create();

    public EBHWYLA() {
        features.add(new FeatureEBInService());
        //features.add(new FeatureEBPower());
    }

    public static List<Feature> getFeatures() {
        return features;
    }
}
