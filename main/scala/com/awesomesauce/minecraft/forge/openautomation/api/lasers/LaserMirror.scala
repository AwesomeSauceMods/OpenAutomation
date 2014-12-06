package com.awesomesauce.minecraft.forge.openautomation.api.lasers

import net.minecraftforge.common.util.ForgeDirection

trait LaserMirror extends LaserReciever {
  var dir1: ForgeDirection
  var dir2: ForgeDirection
}
