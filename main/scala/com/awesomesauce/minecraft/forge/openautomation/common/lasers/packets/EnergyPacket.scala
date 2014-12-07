package com.awesomesauce.minecraft.forge.openautomation.common.lasers.packets

import cofh.api.energy.IEnergyHandler
import com.awesomesauce.minecraft.forge.openautomation.api.lasers.LaserPacket
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.OpenAutomationLasers
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

class EnergyPacket(var amount: Double) extends LaserPacket {
  amount = amount - amount * OpenAutomationLasers.energyLaserMultiple
  def arrive(world: World, x: Int, y: Int, z: Int, to: ForgeDirection): Boolean = {
    val te = world.getTileEntity(x + to.offsetX, y + to.offsetY, z + to.offsetZ)
    if (te.isInstanceOf[IEnergyHandler]) {
      return te.asInstanceOf[IEnergyHandler].receiveEnergy(to.getOpposite, amount.floor.toInt, false) > 0
    }
    false
  }

  def split(sAmount: Int) = {
    val arr = new Array[LaserPacket](sAmount)
    for (i <- Range(0, sAmount)) {
      arr(i) = new EnergyPacket(amount / sAmount)
    }
    arr
  }
}
