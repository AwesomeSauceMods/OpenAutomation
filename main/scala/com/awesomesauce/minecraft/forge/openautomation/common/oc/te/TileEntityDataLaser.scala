package com.awesomesauce.minecraft.forge.openautomation.common.oc.te

import com.awesomesauce.minecraft.forge.openautomation.common.lasers.LaserHelper
import com.awesomesauce.minecraft.forge.openautomation.common.oc.DataPacket
import li.cil.oc.api.Network
import li.cil.oc.api.machine.{Arguments, Callback, Context}
import li.cil.oc.api.network.Visibility
import li.cil.oc.api.prefab.TileEntityEnvironment
import net.minecraftforge.common.util.ForgeDirection

class TileEntityDataLaser extends TileEntityEnvironment {
  val node_ = Network.newNode(this, Visibility.Network).withComponent("dataLaser").withConnector(1000).create()
  node = node_

  @Callback
  def sendLaser(context: Context, arguments: Arguments): Array[AnyRef] = {
    Array(LaserHelper.sendLaser(worldObj, xCoord, yCoord, zCoord,
      ForgeDirection.getOrientation(arguments.checkInteger(0)), new DataPacket(arguments.checkAny(1))))
  }
}
