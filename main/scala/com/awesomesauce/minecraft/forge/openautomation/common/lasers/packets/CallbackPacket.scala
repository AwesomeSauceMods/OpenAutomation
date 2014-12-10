package com.awesomesauce.minecraft.forge.openautomation.common.lasers.packets

import com.awesomesauce.minecraft.forge.openautomation.api.lasers.{LaserCallback, LaserPacket}
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

class CallbackPacket(sender: TileEntity, callback: LaserCallback) extends LaserPacket {
  def arrive(world: World, x: Int, y: Int, z: Int, to: ForgeDirection) = {
    callback.executeCallback(sender, world, x, y, z, to)
    true
  }

  def split(amount: Int) = {
    val arr = new Array[LaserPacket](amount)
    for (i <- Range(0, amount)) {
      arr(i) = new CallbackPacket(sender, callback)
    }
    arr
  }
}
