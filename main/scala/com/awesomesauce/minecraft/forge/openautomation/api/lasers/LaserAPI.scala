package com.awesomesauce.minecraft.forge.openautomation.api.lasers

import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

import scala.collection.mutable.ArrayBuffer

object NullCallback extends LaserCallback {
  def getName = "Nothing"

  def getDescription = "Do Nothing."

  def isUseableOn(destWorld: World, destX: Int, destY: Int, destZ: Int, destTo: ForgeDirection): Boolean = true

  def isExecutable(callbackEmitter: TileEntity, destWorld: World, destX: Int, destY: Int, destZ: Int, destTo: ForgeDirection): Boolean = true

  def executeCallback(callbackEmitter: TileEntity, destWorld: World, destX: Int, destY: Int, destZ: Int, destTo: ForgeDirection) = {}
}

object LaserAPI {
  val callbacks: ArrayBuffer[LaserCallback] = ArrayBuffer[LaserCallback](NullCallback)

  def registerCallback(callback: LaserCallback) = {
    callbacks.append(callback)
  }
}
