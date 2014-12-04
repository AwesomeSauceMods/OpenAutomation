package com.awesomesauce.minecraft.forge.openautomation.common.oc.te

import cofh.api.energy.{IEnergyProvider, IEnergyReceiver}
import com.awesomesauce.minecraft.forge.core.lib.item.BasicDismantleableTile
import li.cil.oc.api.Network
import li.cil.oc.api.machine.{Arguments, Callback, Context}
import li.cil.oc.api.network.Visibility
import li.cil.oc.api.prefab.TileEntityEnvironment
import net.minecraftforge.common.util.ForgeDirection


class TileEntityPowerOutput extends TileEntityEnvironment with IEnergyProvider with BasicDismantleableTile {

  val node_ = Network.newNode(this, Visibility.Network).withComponent("powerOutput").withConnector(1000).create()
  node = node_
  var maxOutput = 1000

  def canConnectEnergy(side: ForgeDirection) = true

  @Callback(setter = true)
  def maxOutput(context: Context, arguments: Arguments): Array[AnyRef] = {
    if (arguments.checkInteger(0) < 1000)
      maxOutput = arguments.checkInteger(0)
    Array(maxOutput.asInstanceOf[Integer])
  }

  override def updateEntity() = {
    super.updateEntity()
    if (node_ != null) {
      for (side <- ForgeDirection.VALID_DIRECTIONS) {
        val te = worldObj.getTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ)
        if (te.isInstanceOf[IEnergyReceiver]) {
          val energyHandler = te.asInstanceOf[IEnergyReceiver]
          val amountOfEnergyToDrain = energyHandler.receiveEnergy(side.getOpposite, maxOutput * 10, true).toDouble / 10
          val drainedEnergy = extractEnergy(side, amountOfEnergyToDrain.toInt, false)
          val transferred = energyHandler.receiveEnergy(side.getOpposite, am, false)
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

  def extractEnergy(side: ForgeDirection, maxExtract: Int, simulate: Boolean): Int = {
    if (simulate) {
      if (maxExtract < maxOutput) {
        (node_.localBuffer() * 10).toInt
      }
      else {
        maxOutput * 10
      }
    }
    else {
      if (maxExtract < maxOutput) {
        (node_.changeBuffer(-maxExtract) * 10).toInt
      }
      else {
        (node_.changeBuffer(-maxOutput) * 10).toInt
      }
    }
  }
}
