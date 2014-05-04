package com.awesomesauce.minecraft.forge.openautomation.api.tools

import net.minecraftforge.common.util.ForgeDirection

/**
 * Able to define a side.
 */
trait SideDefinable {
	def setSide(dir: ForgeDirection)
}