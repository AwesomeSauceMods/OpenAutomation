package com.awesomesauce.minecraft.forge.openautomation.api.lasers;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LaserAPI {
    public static List<LaserCallback> callbacks = new ArrayList<LaserCallback>();

    public static void registerCallback(LaserCallback callback) {
        callbacks.add(callback);
    }

    public static Set<ILaserModule> modules = new HashSet<ILaserModule>();

    public static void registerLaserModule(ILaserModule module) {
        modules.add(module);
    }

    public static boolean isModule(ItemStack stack) {
        for (ILaserModule module : modules) {
            if (module.stackIsModule(stack))
                return true;
        }
        return false;
    }

    public static ILaserModule getModule(ItemStack stack) {
        for (ILaserModule module : modules) {
            if (module.stackIsModule(stack))
                return module;
        }
        return null;
    }
}
