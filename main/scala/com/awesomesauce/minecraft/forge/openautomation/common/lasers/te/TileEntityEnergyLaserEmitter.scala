package com.awesomesauce.minecraft.forge.openautomation.common.lasers.te

import cofh.api.energy.IEnergyHandler
import com.awesomesauce.minecraft.forge.core.lib.item.BasicDismantleableTile
import com.awesomesauce.minecraft.forge.core.lib.util.PlayerUtil
import com.awesomesauce.minecraft.forge.openautomation.api.lasers.LaserHelper
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.packets.EnergyPacket
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection

class TileEntityEnergyLaserEmitter extends TileEntity with TLaserEmitter with IEnergyHandler with BasicDismantleableTile {
  var dunnit = 0
  var maxDunnit = 0

  override def activate(player: EntityPlayer, side: Int, partx: Float, party: Float, partz: Float): Boolean = {
    if (super.activate(player, side, partx, party, partz))
      return true
    PlayerUtil.sendChatMessage(player, dunnit.toString + "RF sent last laser out of")
    PlayerUtil.sendChatMessage(player, maxDunnit.toString + "RF received last laser.")
    false
  }

  def extractEnergy(from: ForgeDirection, maxReceive: Int, simulate: Boolean) = 0

  def getEnergyStored(from: ForgeDirection) = 0

  def getMaxEnergyStored(from: ForgeDirection) = 10000

  def receiveEnergy(from: ForgeDirection, maxReceive: Int, simulate: Boolean) = {
    maxDunnit = maxReceive
    if (LaserHelper.sendLaser(worldObj, xCoord, yCoord, zCoord, from.getOpposite, new EnergyPacket(maxReceive))) {
      dunnit = maxReceive
      maxReceive
    }
    else {
      dunnit = 0
      0
    }
  }

  def canConnectEnergy(from: ForgeDirection) = true
}
