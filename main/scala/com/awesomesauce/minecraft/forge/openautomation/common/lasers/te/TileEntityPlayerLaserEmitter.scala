package com.awesomesauce.minecraft.forge.openautomation.common.lasers.te

import com.awesomesauce.minecraft.forge.core.lib.item.{BasicDismantleableTile, TActivatedTileEntity}
import com.awesomesauce.minecraft.forge.core.lib.util.PlayerUtil
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.packets.EntityPacket
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.{LaserHelper, OpenAutomationLasers}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection

class TileEntityPlayerLaserEmitter extends TileEntity with TEnergyReceiver with TActivatedTileEntity with BasicDismantleableTile {
  val energyStorageAmount = OpenAutomationLasers.playerLaserStorage
  val energyCost = OpenAutomationLasers.playerLaserCost

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
