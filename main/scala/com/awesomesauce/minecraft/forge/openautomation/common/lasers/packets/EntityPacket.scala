package com.awesomesauce.minecraft.forge.openautomation.common.lasers.packets

import com.awesomesauce.minecraft.forge.openautomation.api.lasers.LaserPacket
import net.minecraft.entity.EntityLivingBase
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

class EntityPacket(entity: EntityLivingBase) extends LaserPacket {
  def arrive(world: World, x: Int, y: Int, z: Int, to: ForgeDirection) = {
    if (!world.isAirBlock(x + to.offsetX, y + to.offsetY, z + to.offsetZ))
      false
    else {
      entity.setPositionAndUpdate(x + to.offsetX + 0.5, y + to.offsetY, z + to.offsetZ + 0.5)
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
