package com.awesomesauce.minecraft.forge.openautomation.common.oc.driver

import com.awesomesauce.minecraft.forge.openautomation.api.lasers.LaserMirror
import li.cil.oc.api.Network
import li.cil.oc.api.machine.{Arguments, Callback, Context}
import li.cil.oc.api.network.Visibility
import li.cil.oc.api.prefab.{DriverTileEntity, ManagedEnvironment}
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

object DriverLaserMirror extends DriverTileEntity {
  def getTileEntityClass = classOf[LaserMirror]

  def createEnvironment(world: World, x: Int, y: Int, z: Int) = {
    new LaserMirrorEnvironment(world.getTileEntity(x, y, z).asInstanceOf[LaserMirror])
  }
}

class LaserMirrorEnvironment(lm: LaserMirror) extends ManagedEnvironment {
  setNode(Network.newNode(this, Visibility.Network).withComponent("laserMirror").create())

  @Callback(doc = "function():integer -- Get the first side of the mirror.")
  def getDir1(context: Context, arguments: Arguments): Array[AnyRef] = {
    Array(lm.dir1.ordinal().asInstanceOf[Integer])
  }

  @Callback(doc = "function():integer -- Get the second side of the mirror.")
  def getDir2(context: Context, arguments: Arguments): Array[AnyRef] = {
    Array(lm.dir2.ordinal().asInstanceOf[Integer])
  }

  @Callback(doc = "function(integer) -- Set the first side of the mirror.")
  def setDir1(context: Context, arguments: Arguments): Array[AnyRef] = {
    lm.dir1 = ForgeDirection.getOrientation(arguments.checkInteger(0))
    Array(null)
  }

  @Callback(doc = "function(integer) -- Set the second side of the mirror.")
  def setDir2(context: Context, arguments: Arguments): Array[AnyRef] = {
    lm.dir2 = ForgeDirection.getOrientation(arguments.checkInteger(0))
    Array(null)
  }
}
