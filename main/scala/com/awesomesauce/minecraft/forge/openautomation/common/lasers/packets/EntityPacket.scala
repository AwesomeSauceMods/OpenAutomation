package com.awesomesauce.minecraft.forge.openautomation.common.lasers.packets

import com.awesomesauce.minecraft.forge.openautomation.api.lasers.LaserPacket
import net.minecraft.entity.Entity
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

class EntityPacket(entity: Entity) extends LaserPacket {
  def arrive(world: World, x: Int, y: Int, z: Int, to: ForgeDirection) = {
    if (world.getBlock(x + to.offsetX, y + to.offsetY, z + to.offsetZ).isCollidable)
      false
    else {
      entity.setPosition(x + to.offsetX, y + to.offsetY, z + to.offsetZ)
      true
    }
  }

  def split(amount: Int) = {
    val arr = new Array[LaserPacket](amount)
    for (i <- Range(0, amount)) {
      arr(i) = new EntityPacket(entity)
    }
    arr
  }
}
