package com.awesomesauce.minecraft.forge.openautomation.api.tools

import net.minecraft.item.ItemStack

trait CardAppliable {
	def applyCard(card:ItemStack)
}