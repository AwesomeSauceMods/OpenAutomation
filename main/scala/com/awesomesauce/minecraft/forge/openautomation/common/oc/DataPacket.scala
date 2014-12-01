package com.awesomesauce.minecraft.forge.openautomation.common.oc

import com.awesomesauce.minecraft.forge.openautomation.api.lasers.LaserPacket
import li.cil.oc.api.network.Environment
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

class DataPacket(data: Object) extends LaserPacket {
  def arrive(world: World, x: Int, y: Int, z: Int, to: ForgeDirection) = {
    val te = world.getTileEntity(x + to.offsetX, y + to.offsetY, z + to.offsetZ)
    if (te.isInstanceOf[Environment]) {
      val env = te.asInstanceOf[Environment]
      env.node.sendToReachable("computer.signal", "laserMessage", data)
      true
    }
    else {
      false
    }
  }

  def split(sAmount: Int) = {
    val arr = new Array[LaserPacket](sAmount)
    for (i <- Range(0, sAmount)) {
      arr(i) = new DataPacket(data)
    }
    arr
  }
}
