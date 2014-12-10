package com.awesomesauce.minecraft.forge.openautomation.api.lasers

import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

trait LaserCallback {
  /**
   * Returns a period separated identifier for the callback
   * (Not neccessary, but that is the way the official OA callbacks work.)
   * e.g. "Laser.Mirror.Rotate.Side1"
   * @return period separated identifier for the callback.
   */
  def getName: String

  /**
   * Returns a human readable description for what the callback does.
   * @return Human readable description for what the callback does.
   */
  def getDescription: String

  /**
   * Returns if the laser is useable on a block.
   * This should return true if it ever will be useable.
   * @param destWorld The world of the destination
   * @param destX The x position of the destination.
   * @param destY The x position of the destination.
   * @param destZ The x position of the destination.
   * @param destTo The x position of the destination.
   * @return If the laser works on a block
   */
  def isUseableOn(destWorld: World, destX: Int, destY: Int, destZ: Int, destTo: ForgeDirection): Boolean

  /**
   * Returns if the callback can be run at the moment.
   * Difference between this and isUseableOn is that this is called to determine if
   * any special circumstances are required to be used. This includes redstone signals to activate them, etc.
   * @param callbackEmitter The callback emitter tile entity.
   * @param destWorld The world of the destination.
   * @param destX The x position of the destination.
   * @param destY The x position of the destination.
   * @param destZ The x position of the destination.
   * @param destTo The x position of the destination.
   * @return If the laser works on a block
   */
  def isExecutable(callbackEmitter: TileEntity, destWorld: World, destX: Int, destY: Int, destZ: Int, destTo: ForgeDirection): Boolean

  /**
   * Returns if the laser is useable on a block.
   * @param callbackEmitter The callback emitter tile entity.
   * @param destWorld The world of the destination
   * @param destX The x position of the destination.
   * @param destY The x position of the destination.
   * @param destZ The x position of the destination.
   * @param destTo The x position of the destination.
   * @return If the laser works on a block
   */
  def executeCallback(callbackEmitter: TileEntity, destWorld: World, destX: Int, destY: Int, destZ: Int, destTo: ForgeDirection)
}