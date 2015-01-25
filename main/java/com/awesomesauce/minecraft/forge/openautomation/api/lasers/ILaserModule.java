package com.awesomesauce.minecraft.forge.openautomation.api.lasers;


import net.minecraft.item.ItemStack;

public interface ILaserModule {
    boolean stackIsModule(ItemStack stack);

    void modifyPacket(LaserPacket packet);

    void modifyPacketAfterArrival(LaserPacket packet);
}
