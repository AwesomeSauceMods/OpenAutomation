package com.awesomesauce.minecraft.forge.openautomation.addons.tile

import cofh.api.energy.{IEnergyConnection, IEnergyHandler}
import li.cil.oc.api.Network
import li.cil.oc.api.network.Visibility
import li.cil.oc.api.prefab.TileEntityEnvironment
import net.minecraftforge.common.util.ForgeDirection


class TileEntityPowerOutput extends TileEntityEnvironment with IEnergyConnection {
  val node_ = Network.newNode(this, Visibility.Network).withComponent("powerOutput").withConnector(10000).create()
  node = node_

  def canConnectEnergy(side: ForgeDirection) = true

  override def updateEntity() = {
    super.updateEntity()
    for (side <- ForgeDirection.VALID_DIRECTIONS) {
      val te = worldObj.getTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ)
      if (te.isInstanceOf[IEnergyHandler]) {
        val energyHandler = te.asInstanceOf[IEnergyHandler]
        energyHandler.receiveEnergy(side.getOpposite, (this.node_.changeBuffer(energyHandler.getMaxEnergyStored(side.getOpposite) - energyHandler.getEnergyStored(side.getOpposite).toDouble / 10) * 10).toInt, false)
      }
    }
  }
}
