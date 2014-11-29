package com.awesomesauce.minecraft.forge.openautomation.common.lasers

import com.awesomesauce.minecraft.forge.openautomation.api.lasers.{LaserPacket, LaserReciever}
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

object LaserHelper {
  def sendLaser(world: World, x: Int, y: Int, z: Int, dir: ForgeDirection, packet: LaserPacket): Boolean = {
    var nx = x + dir.offsetX
    var ny = y + dir.offsetY
    var nz = z + dir.offsetZ
    while (!world.getBlock(nx, ny, nz).isOpaqueCube) {
      nx += dir.offsetX
      ny += dir.offsetY
      nz += dir.offsetZ
      world.spawnParticle("reddust", nx + 0.5, ny + 0.5, nz + 0.5, 0, 0, 0)
    }
    if (world.getTileEntity(nx, ny, nz).isInstanceOf[LaserReciever]) {
      return world.getTileEntity(nx, ny, nz).asInstanceOf[LaserReciever].arrive(dir.getOpposite, packet)
    }
    false
  }
}
