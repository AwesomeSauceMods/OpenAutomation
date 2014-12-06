package com.awesomesauce.minecraft.forge.openautomation.common.lasers.te

import cofh.api.energy.{EnergyStorage, IEnergyHandler}
import com.awesomesauce.minecraft.forge.core.lib.item.{BasicDismantleableTile, TActivatedTileEntity}
import com.awesomesauce.minecraft.forge.core.lib.util.PlayerUtil
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.LaserHelper
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.packets.EntityPacket
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection

class TileEntityPlayerLaserEmitter extends TileEntity with IEnergyHandler with TActivatedTileEntity with BasicDismantleableTile {
  val energyStorage = new EnergyStorage(1000000)
  val energyCost = 100000

  def extractEnergy(from: ForgeDirection, maxReceive: Int, simulate: Boolean) = 0

  def getEnergyStored(from: ForgeDirection) = energyStorage.getEnergyStored

  def getMaxEnergyStored(from: ForgeDirection) = energyStorage.getMaxEnergyStored

  def receiveEnergy(from: ForgeDirection, maxReceive: Int, simulate: Boolean) = energyStorage.receiveEnergy(maxReceive, simulate)

  def canConnectEnergy(from: ForgeDirection) = true

  def activate(player: EntityPlayer, side: Int, px: Float, py: Float, pz: Float) = {
    if (energyStorage.getEnergyStored >= energyCost) {
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
