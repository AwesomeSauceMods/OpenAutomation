package com.awesomesauce.minecraft.forge.openautomation.api.lasers;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class LaserPacket {
    NBTTagCompound compound = new NBTTagCompound();

    public abstract boolean arrive(World world, int x, int y, int z, ForgeDirection to);

    public abstract LaserPacket[] split(int amount);

    public String particleEffect() {
        return "reddust";
    }

    public NBTTagCompound getCompound() {
        return compound;
    }
}