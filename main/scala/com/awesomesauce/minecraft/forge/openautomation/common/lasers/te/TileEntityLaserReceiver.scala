package com.awesomesauce.minecraft.forge.openautomation.common.lasers.te

import cofh.api.energy.IEnergyConnection
import com.awesomesauce.minecraft.forge.core.lib.item.BasicDismantleableTile
import com.awesomesauce.minecraft.forge.openautomation.api.lasers.{LaserPacket, LaserReciever}
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection

class TileEntityLaserReceiver extends TileEntity with IEnergyConnection with LaserReciever with BasicDismantleableTile {
  def canConnectEnergy(from: ForgeDirection) = true

  def arrive(from: ForgeDirection, packet: LaserPacket) = packet.arrive(worldObj, xCoord, yCoord, zCoord, from.getOpposite)
}
