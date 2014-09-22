package com.awesomesauce.minecraft.forge.openautomation.addons.tile

import cofh.api.energy.{IEnergyConnection, IEnergyHandler}
import li.cil.oc.api.Network
import li.cil.oc.api.network.{Arguments, Callback, Context, Visibility}
import li.cil.oc.api.prefab.TileEntityEnvironment
import net.minecraftforge.common.util.ForgeDirection


class TileEntityPowerOutput extends TileEntityEnvironment with IEnergyConnection {
  val node_ = Network.newNode(this, Visibility.Network).withComponent("powerOutput").withConnector(1000).create()
  node = node_
  var maxOutput = 1000

  def canConnectEnergy(side: ForgeDirection) = true

  @Callback
  def setOutput(context: Context, arguments: Arguments): Array[AnyRef] = {
    if (arguments.checkInteger(0) < 1000)
      maxOutput = arguments.checkInteger(0)
    Array(maxOutput.asInstanceOf[Integer])
  }
  override def updateEntity() = {
    super.updateEntity()
    if (node_ != null) {
      for (side <- ForgeDirection.VALID_DIRECTIONS) {
        val te = worldObj.getTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ)
        if (te.isInstanceOf[IEnergyHandler]) {
          val energyHandler = te.asInstanceOf[IEnergyHandler]
          val amountOfEnergyToDrain = energyHandler.receiveEnergy(side.getOpposite, maxOutput * 10, true).toDouble / 10
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
