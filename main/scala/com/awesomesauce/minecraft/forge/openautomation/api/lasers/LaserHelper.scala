package com.awesomesauce.minecraft.forge.openautomation.api.lasers

import cpw.mods.fml.common.Loader
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

object LaserHelper {
  val computronicsLoaded = Loader.isModLoaded("Computronics")
  def sendLaser(world: World, x: Int, y: Int, z: Int, dir: ForgeDirection, packet: LaserPacket): Boolean = {
    var nx = x + dir.offsetX
    var ny = y + dir.offsetY
    var nz = z + dir.offsetZ
    var counter = 0
    while (!world.getBlock(nx, ny, nz).isOpaqueCube && counter < 400) {
      counter += 1
      nx += dir.offsetX
      ny += dir.offsetY
      nz += dir.offsetZ
      if (computronicsLoaded)
        pl.asie.computronics.util.ParticleUtils.sendParticlePacket("reddust", world, nx, ny, nz, 0,0,0)
    }
    if (world.getTileEntity(nx, ny, nz).isInstanceOf[LaserReciever]) {
      return world.getTileEntity(nx, ny, nz).asInstanceOf[LaserReciever].arrive(dir.getOpposite, packet)
    }
    false
  }
}
