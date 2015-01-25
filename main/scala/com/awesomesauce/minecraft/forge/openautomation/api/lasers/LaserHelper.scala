package com.awesomesauce.minecraft.forge.openautomation.api.lasers

import cpw.mods.fml.common.Loader
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

object LaserHelper {
  lazy val computronicsLoaded = Loader.isModLoaded("computronics")
  def sendLaser(world: World, x: Int, y: Int, z: Int, dir: ForgeDirection, packet: LaserPacket): Boolean = {
    var nx = x + dir.offsetX
    var ny = y + dir.offsetY
    var nz = z + dir.offsetZ
    var counter = 0
    while (!world.getBlock(nx, ny, nz).isOpaqueCube && counter < 512) {
      if (computronicsLoaded)
        pl.asie.computronics.util.ParticleUtils.sendParticlePacket(packet.particleEffect, world, nx + 0.5, ny + 0.5, nz + 0.5, 0, 0, 0)
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
