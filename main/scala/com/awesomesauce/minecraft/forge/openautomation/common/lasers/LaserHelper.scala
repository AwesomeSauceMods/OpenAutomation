package com.awesomesauce.minecraft.forge.openautomation.common.lasers

import com.awesomesauce.minecraft.forge.openautomation.api.lasers.{LaserPacket, LaserReciever}
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

object LaserHelper {
  def sendLaser(world: World, x: Int, y: Int, z: Int, dir: ForgeDirection, packet: LaserPacket): Boolean = {
    for (i <- Range(0, 10))
      world.spawnParticle("reddust", x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, 0, 0, 0)
    var nx = x + dir.offsetX
    var ny = y + dir.offsetY
    var nz = z + dir.offsetZ
    var counter = 0
    while (!world.getBlock(nx, ny, nz).isOpaqueCube && counter < 400) {
      world.spawnParticle("reddust", nx - 0.5D, ny - 0.5D, nz - 0.5D, world.rand.nextGaussian() * 0.02D, world.rand.nextGaussian() * 0.02D, world.rand.nextGaussian() * 0.02D)
      counter += 1
      nx += dir.offsetX
      ny += dir.offsetY
      nz += dir.offsetZ
    }
    if (world.getTileEntity(nx, ny, nz).isInstanceOf[LaserReciever]) {
      return world.getTileEntity(nx, ny, nz).asInstanceOf[LaserReciever].arrive(dir.getOpposite, packet)
    }
    false
  }
}
