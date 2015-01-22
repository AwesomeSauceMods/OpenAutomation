package com.awesomesauce.minecraft.forge.openautomation.api.lasers

import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

trait LaserPacket {
  def arrive(world: World, x: Int, y: Int, z: Int, to: ForgeDirection): Boolean

  def split(amount: Int): Array[LaserPacket]

  def particleEffect: String = "reddust"
}