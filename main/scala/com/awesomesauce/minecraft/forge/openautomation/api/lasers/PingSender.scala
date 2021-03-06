package com.awesomesauce.minecraft.forge.openautomation.api.lasers

import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

trait PingSender {
  def pingArrive(world: World, x: Int, y: Int, z: Int, to: ForgeDirection)
}
