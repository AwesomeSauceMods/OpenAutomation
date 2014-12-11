package com.awesomesauce.minecraft.forge.openautomation.common.oc.driver

import com.awesomesauce.minecraft.forge.openautomation.api.lasers.{LaserAPI, LaserHelper}
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.packets.CallbackPacket
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.te.TileEntityCallbackLaserEmitter
import li.cil.oc.api.Network
import li.cil.oc.api.machine.{Arguments, Callback, Context}
import li.cil.oc.api.network.Visibility
import li.cil.oc.api.prefab.{DriverTileEntity, ManagedEnvironment}
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

object DriverCallbackLaserEmitter extends DriverTileEntity {
  def getTileEntityClass = classOf[TileEntityCallbackLaserEmitter]

  def createEnvironment(world: World, x: Int, y: Int, z: Int) = {
    new CallbackLaserEmitterEnvironment(world.getTileEntity(x, y, z).asInstanceOf[TileEntityCallbackLaserEmitter])
  }
}

class CallbackLaserEmitterEnvironment(emitter: TileEntityCallbackLaserEmitter) extends ManagedEnvironment {
  setNode(Network.newNode(this, Visibility.Network).withComponent("callbackLaserEmitter").create())

  @Callback(doc = "function():string -- Get the name of the current callback.")
  def getCallback(context: Context, arguments: Arguments): Array[AnyRef] = {
    Array(emitter.currentCallback.getName)
  }

  @Callback(doc = "function():string -- Get the description of the current callback.")
  def getDescription(context: Context, arguments: Arguments): Array[AnyRef] = {
    Array(emitter.currentCallback.getDescription)
  }

  @Callback(doc = "function(string):boolean -- Set the current callback. If a callback exists by that name, set it to that, else do nothing.")
  def setCallback(context: Context, arguments: Arguments): Array[AnyRef] = {
    for (i <- Range(0, LaserAPI.callbacks.length)) {
      if (LaserAPI.callbacks(i).getName.equalsIgnoreCase(arguments.checkString(0))) {
        emitter.currentCallbackNum = i
        emitter.currentCallback = LaserAPI.callbacks(i)
      }
    }
  }

  @Callback(doc = "function(integer):boolean -- Send callback laser on a side. Returns whether it hit or not.")
  def sendLaser(context: Context, arguments: Arguments): Array[AnyRef] = {
    Array(LaserHelper.sendLaser(emitter.getWorldObj, emitter.xCoord, emitter.yCoord, emitter.zCoord, ForgeDirection.getOrientation(arguments.checkInteger(0)), new CallbackPacket(emitter, emitter.currentCallback)).asInstanceOf[java.lang.Boolean])
  }
}
