package com.awesomesauce.minecraft.forge.openautomation.common.lasers.te

import cofh.api.energy.IEnergyHandler
import com.awesomesauce.minecraft.forge.openautomation.api.lasers.EnergyPacket
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.LaserHelper
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection


class TileEntityEnergyLaserEmitter extends TileEntity with IEnergyHandler {
  def extractEnergy(from: ForgeDirection, maxExtract: Int, simulate: Boolean) = 0

  def getEnergyStored(from: ForgeDirection) = 0

  def getMaxEnergyStored(from: ForgeDirection) = 10000

  def receiveEnergy(from: ForgeDirection, maxReceive: Int, simulate: Boolean) = {
    if (LaserHelper.sendLaser(worldObj, xCoord, yCoord, zCoord, from.getOpposite, new EnergyPacket(maxReceive)))
      maxReceive
    else
      0
  }

  def canConnectEnergy(from: ForgeDirection) = true
}