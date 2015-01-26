package com.awesomesauce.minecraft.forge.openautomation.common.lasers.te

import com.awesomesauce.minecraft.forge.core.lib.item.BasicDismantleableTile
import com.awesomesauce.minecraft.forge.core.lib.util.PlayerUtil
import com.awesomesauce.minecraft.forge.openautomation.api.lasers.LaserHelper
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.OpenAutomationLasers
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.packets.{EntityPacket, PingPacket}
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.common.util.ForgeDirection

class TileEntityPlayerLaserEmitter extends TileEnergyReceiver with TLaserEmitter with BasicDismantleableTile {
  val energyStorageAmount = OpenAutomationLasers.playerLaserStorage
  val energyCost = OpenAutomationLasers.playerLaserCost
  var updateTicks = 0
  val updateEmitPing = 100
  var sendPing = false

  override def updateEntity() = {
    if (sendPing) {
      updateTicks += 1
      if (updateTicks % updateEmitPing == 0)
        for (side <- ForgeDirection.values())
          LaserHelper.sendLaser(worldObj, xCoord, yCoord, zCoord, side, new PingPacket(null))
    }
  }

  override def activate(player: EntityPlayer, side: Int, px: Float, py: Float, pz: Float): Boolean = {
    if (super.activate(player, side, px, py, pz))
      return true
    if (player.isSneaking) {
      sendPing = !sendPing
      PlayerUtil.sendChatMessage(player, "Sending guide laser: " + sendPing.toString)
      return false
    }
    if (consumeEnergy()) {
      if (LaserHelper.sendLaser(worldObj, xCoord, yCoord, zCoord, ForgeDirection.getOrientation(side).getOpposite, new EntityPacket(player))) {
        //PlayerUtil.sendChatMessage(player, "Successfully sent.")
        worldObj.playSoundAtEntity(player, "mob.endermen.portal", 1.0F, 1.0F)
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
