package com.awesomesauce.minecraft.forge.openautomation.addons.tile

import cofh.api.energy.{IEnergyConnection, IEnergyHandler}
import li.cil.oc.api.Network
import li.cil.oc.api.network.Visibility
import li.cil.oc.api.prefab.TileEntityEnvironment
import net.minecraftforge.common.util.ForgeDirection


class TileEntityPowerOutput extends TileEntityEnvironment with IEnergyConnection {
  val node_ = Network.newNode(this, Visibility.Network).withComponent("powerOutput").withConnector(1000).create()
  node = node_

  def canConnectEnergy(side: ForgeDirection) = true

  override def updateEntity() = {
    super.updateEntity()
    if (node_ != null) {
      for (side <- ForgeDirection.VALID_DIRECTIONS) {
        val te = worldObj.getTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ)
        if (te.isInstanceOf[IEnergyHandler]) {
          val energyHandler = te.asInstanceOf[IEnergyHandler]
          val amountOfEnergyToDrain = energyHandler.receiveEnergy(side.getOpposite, 10000, true).toDouble / 10
          val bufferChanged = this.node_.changeBuffer(-amountOfEnergyToDrain)
          val amountDrained = ((amountOfEnergyToDrain + bufferChanged) * 10).toInt
          val transferred = energyHandler.receiveEnergy(side.getOpposite, amountDrained, false)
          /*
          println("aOETD:"+amountOfEnergyToDrain)
          println("bC:"+bufferChanged)
          println("aD:"+amountDrained)
          println("t:"+transferred)
          println("s:"+side.toString)
          */
        }
      }
    }
  }
}
