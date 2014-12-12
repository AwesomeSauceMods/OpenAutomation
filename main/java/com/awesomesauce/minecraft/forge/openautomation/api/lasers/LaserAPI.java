package com.awesomesauce.minecraft.forge.openautomation.api.lasers;

import java.util.ArrayList;
import java.util.List;

public class LaserAPI {
    public static List<LaserCallback> callbacks = new ArrayList<LaserCallback>();

    public static void registerCallback(LaserCallback callback) {
        callbacks.add(callback);
    }
}
