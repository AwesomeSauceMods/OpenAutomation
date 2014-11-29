package com.awesomesauce.minecraft.forge.openautomation.api.lasers

import net.minecraftforge.common.util.ForgeDirection

trait LaserReciever {
  def arrive(from: ForgeDirection, laser: LaserPacket): Boolean
}
