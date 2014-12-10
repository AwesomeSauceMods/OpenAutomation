package com.awesomesauce.minecraft.forge.openautomation.common.lasers.te

import com.awesomesauce.minecraft.forge.core.lib.item.TActivatedTileEntity
import com.awesomesauce.minecraft.forge.core.lib.util.PlayerUtil
import com.awesomesauce.minecraft.forge.openautomation.api.lasers.{LaserAPI, LaserCallback, LaserHelper, PingSender}
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.packets.{CallbackPacket, PingPacket}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

class TileEntityCallbackLaserEmitter extends TileEntity with TEnergyReceiver with TActivatedTileEntity with PingSender {
  val energyCost = 10
  val energyStorageAmount = 1000
  var enabled = false
  var currentCallbackNum = 0
  var currentCallback: LaserCallback = LaserAPI.callbacks(0)
  var side: ForgeDirection = ForgeDirection.UNKNOWN
  var pingPlayer: EntityPlayer = null

  override def updateEntity() = {
    if (consumeEnergy()) {
      if (enabled) {
        LaserHelper.sendLaser(worldObj, xCoord, yCoord, zCoord, side, new CallbackPacket(this, currentCallback))
      }
    }
  }

  def pingArrive(world: World, x: Int, y: Int, z: Int, to: ForgeDirection) = {
    currentCallbackNum += 1
    currentCallback = LaserAPI.callbacks(currentCallbackNum)
    while (!currentCallback.isUseableOn(world, x, y, z, to)) {
      currentCallbackNum += 1
      currentCallback = LaserAPI.callbacks(currentCallbackNum)
    }
  }

  def activate(player: EntityPlayer, s: Int, partx: Float, party: Float, partz: Float) = {
    if (player.isSneaking) {
      enabled = !enabled
      PlayerUtil.sendChatMessage(player, currentCallback.getName + " now enabled: " + enabled.toString)
      true
    }
    else {
      side = ForgeDirection.getOrientation(s).getOpposite
      pingPlayer = player
      LaserHelper.sendLaser(worldObj, xCoord, yCoord, zCoord, side, new PingPacket(this))
      false
    }
  }
}
