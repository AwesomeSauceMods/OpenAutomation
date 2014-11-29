package com.awesomesauce.minecraft.forge.openautomation.common.lasers.te

import cofh.api.energy.IEnergyConnection
import com.awesomesauce.minecraft.forge.openautomation.api.lasers.{LaserPacket, LaserReciever}
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection

/**
 * Created by gjgfuj on 11/29/14.
 */
class TileEntityLaserReceiver extends TileEntity with IEnergyConnection with LaserReciever {
  def canConnectEnergy(from: ForgeDirection) = true

  def arrive(from: ForgeDirection, packet: LaserPacket) = packet.arrive(worldObj, xCoord, yCoord, zCoord, from.getOpposite)
}
