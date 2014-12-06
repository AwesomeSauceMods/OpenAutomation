package com.awesomesauce.minecraft.forge.openautomation.common.lasers.te

import cofh.api.energy.IEnergyHandler
import com.awesomesauce.minecraft.forge.core.lib.item.{BasicDismantleableTile, TActivatedTileEntity}
import com.awesomesauce.minecraft.forge.core.lib.util.PlayerUtil
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.LaserHelper
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.packets.EnergyPacket
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection

class TileEntityEnergyLaserEmitter extends TileEntity with IEnergyHandler with BasicDismantleableTile with TActivatedTileEntity {
  var dunnit = false

  def activate(player: EntityPlayer, side: Int, partx: Float, party: Float, partz: Float): Boolean = {
    PlayerUtil.sendChatMessage(player, dunnit.toString)
  }
  def extractEnergy(from: ForgeDirection, maxReceive: Int, simulate: Boolean) = 0
  def getEnergyStored(from: ForgeDirection) = 0

  def getMaxEnergyStored(from: ForgeDirection) = 10000
  def receiveEnergy(from: ForgeDirection, maxReceive: Int, simulate: Boolean) = {
    if (LaserHelper.sendLaser(worldObj, xCoord, yCoord, zCoord, from.getOpposite, new EnergyPacket(maxReceive))) {
      dunnit = true
      maxReceive
    }
    else {
      dunnit = false
      0
    }
  }

  def canConnectEnergy(from: ForgeDirection) = true
}
