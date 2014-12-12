package com.awesomesauce.minecraft.forge.openautomation.api.lasers;


import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface LaserCallback {
    /**
     * Returns a period separated identifier for the callback
     * (Not neccessary, but that is the way the official OA callbacks work.)
     * e.g. "Laser.Mirror.Rotate.Side1"
     *
     * @return period separated identifier for the callback.
     */
    String getName();

    /**
     * Returns a human readable description for what the callback does.
     *
     * @return Human readable description for what the callback does.
     */
    String getDescription();

    /**
     * Returns if the laser is useable on a block.
     * This should return true if it ever will be useable.
     *
     * @param destWorld The world of the destination
     * @param destX     The x position of the destination.
     * @param destY     The x position of the destination.
     * @param destZ     The x position of the destination.
     * @param destTo    The x position of the destination.
     * @return If the laser works on a block
     */
    boolean isUseableOn(World destWorld, int destX, int destY, int destZ, ForgeDirection destTo);

    /**
     * Returns if the callback can be run at the moment.
     * Difference between this and isUseableOn is that this is called to determine if
     * any special circumstances are required to be used. This includes redstone signals to activate them, etc.
     *
     * @param callbackEmitter The callback emitter tile entity.
     * @param destWorld       The world of the destination.
     * @param destX           The x position of the destination.
     * @param destY           The x position of the destination.
     * @param destZ           The x position of the destination.
     * @param destTo          The x position of the destination.
     * @return If the laser works on a block
     */
    boolean isExecutable(TileEntity callbackEmitter, World destWorld, int destX, int destY, int destZ, ForgeDirection destTo);

    /**
     * Execute the callback.
     *
     * @param callbackEmitter The callback emitter tile entity.
     * @param destWorld       The world of the destination
     * @param destX           The x position of the destination.
     * @param destY           The x position of the destination.
     * @param destZ           The x position of the destination.
     * @param destTo          The x position of the destination.
     * @return If the laser works on a block
     */
    boolean executeCallback(TileEntity callbackEmitter, World destWorld, int destX, int destY, int destZ, ForgeDirection destTo);
}