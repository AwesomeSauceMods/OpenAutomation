package com.awesomesauce.minecraft.forge.openautomation.common.oc.te

import com.awesomesauce.minecraft.forge.core.lib.item.BasicDismantleableTile
import com.awesomesauce.minecraft.forge.openautomation.api.lasers.LaserHelper
import com.awesomesauce.minecraft.forge.openautomation.common.oc.DataPacket
import li.cil.oc.api.Network
import li.cil.oc.api.machine.{Arguments, Callback, Context}
import li.cil.oc.api.network.Visibility
import li.cil.oc.api.prefab.TileEntityEnvironment
import net.minecraftforge.common.util.ForgeDirection

class TileEntityDataLaser extends TileEntityEnvironment with BasicDismantleableTile {
  val node_ = Network.newNode(this, Visibility.Network).withComponent("laser").withConnector(1000).create()
  node = node_

  @Callback(doc = "function(side:integer,message:anything):boolean -- Sends a data laser with said packet. The receiving end will receive a signal 'laser_message',address,message")
  def sendDataLaser(context: Context, arguments: Arguments): Array[AnyRef] = {
    node_.tryChangeBuffer(-1)
    Array(LaserHelper.sendLaser(worldObj, xCoord, yCoord, zCoord,
      ForgeDirection.getOrientation(arguments.checkInteger(0)), new DataPacket(arguments.checkAny(1))).asInstanceOf[java.lang.Boolean])
  }
}
