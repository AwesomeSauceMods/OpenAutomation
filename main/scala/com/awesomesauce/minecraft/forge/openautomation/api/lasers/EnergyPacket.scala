package com.awesomesauce.minecraft.forge.openautomation.api.lasers

import cofh.api.energy.IEnergyHandler
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection


class EnergyPacket(amount: Int) extends LaserPacket {
  def arrive(world: World, x: Int, y: Int, z: Int, to: ForgeDirection): Boolean = {
    val te = world.getTileEntity(x + to.offsetX, y + to.offsetY, z + to.offsetZ)
    if (te.isInstanceOf[IEnergyHandler]) {
      return te.asInstanceOf[IEnergyHandler].receiveEnergy(to.getOpposite, amount, false) > 0
    }
    false
  }
}
