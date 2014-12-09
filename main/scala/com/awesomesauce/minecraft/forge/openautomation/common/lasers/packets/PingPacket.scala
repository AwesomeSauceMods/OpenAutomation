package com.awesomesauce.minecraft.forge.openautomation.common.lasers.packets

import com.awesomesauce.minecraft.forge.openautomation.api.lasers.{LaserPacket, PingSender}
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

class PingPacket(sender: PingSender) extends LaserPacket {
  def arrive(world: World, x: Int, y: Int, z: Int, to: ForgeDirection) = {
    sender.pingArrive(world, x, y, z, to)
    true
  }

  def split(amount: Int) = {
    val arr = new Array[LaserPacket](amount)
    for (i <- Range(0, amount)) {
      arr(i) = new PingPacket(sender)
    }
    arr
  }
}
