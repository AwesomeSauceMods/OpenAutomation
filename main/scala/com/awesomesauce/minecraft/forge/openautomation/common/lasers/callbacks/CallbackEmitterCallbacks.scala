package com.awesomesauce.minecraft.forge.openautomation.common.lasers.callbacks

import com.awesomesauce.minecraft.forge.openautomation.api.lasers.{LaserCallback, LaserHelper}
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.packets.PingPacket
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.te.TileEntityCallbackLaserEmitter
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

object LaserCallbackChange extends LaserCallback {

  def getName = "Laser.CallbackEmitter.Change"

  def getDescription = "Go through the callbacks available."

  def isUseableOn(destWorld: World, destX: Int, destY: Int, destZ: Int, destTo: ForgeDirection): Boolean = destWorld.getTileEntity(destX, destY, destZ).isInstanceOf[TileEntityCallbackLaserEmitter]

  def isExecutable(callbackEmitter: TileEntity, destWorld: World, destX: Int, destY: Int, destZ: Int, destTo: ForgeDirection): Boolean = true

  def executeCallback(callbackEmitter: TileEntity, destWorld: World, destX: Int, destY: Int, destZ: Int, destTo: ForgeDirection) = {
    val emitter = destWorld.getTileEntity(destX, destY, destZ).asInstanceOf[TileEntityCallbackLaserEmitter]
    emitter.pingPlayer = null
    LaserHelper.sendLaser(destWorld, destX, destY, destZ, destTo, new PingPacket(emitter))
  }
}
