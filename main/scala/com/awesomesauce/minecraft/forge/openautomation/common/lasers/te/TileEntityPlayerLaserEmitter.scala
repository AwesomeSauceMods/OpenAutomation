package com.awesomesauce.minecraft.forge.openautomation.common.lasers.te

import com.awesomesauce.minecraft.forge.core.lib.item.{BasicDismantleableTile, TActivatedTileEntity}
import com.awesomesauce.minecraft.forge.core.lib.util.PlayerUtil
import com.awesomesauce.minecraft.forge.openautomation.api.lasers.LaserHelper
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.OpenAutomationLasers
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.packets.{PingPacket, EntityPacket}
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.common.util.ForgeDirection

class TileEntityPlayerLaserEmitter extends TileEnergyReceiver with TActivatedTileEntity with BasicDismantleableTile {
  val energyStorageAmount = OpenAutomationLasers.playerLaserStorage
  val energyCost = OpenAutomationLasers.playerLaserCost
  var updateTicks = 0
  val updateEmitPing = 100
  override def updateEntity() = {
    updateTicks += 1
    if (updateTicks % updateEmitPing == 0)
      for (side <- ForgeDirection.values())
        LaserHelper.sendLaser(worldObj, xCoord, yCoord, zCoord, side, new PingPacket(null))
  }
  def activate(player: EntityPlayer, side: Int, px: Float, py: Float, pz: Float) = {
    if (consumeEnergy()) {
      if (LaserHelper.sendLaser(worldObj, xCoord, yCoord, zCoord, ForgeDirection.getOrientation(side).getOpposite, new EntityPacket(player))) {
        PlayerUtil.sendChatMessage(player, "Successfully sent.")
        true
      }
      else {
        PlayerUtil.sendChatMessage(player, "Unable to find a destination.")
        false
      }
    }
    else {
      PlayerUtil.sendChatMessage(player, "Not enough energy to teleport.")
      false
    }
  }
}
