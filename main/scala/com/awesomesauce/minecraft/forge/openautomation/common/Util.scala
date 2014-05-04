package com.awesomesauce.minecraft.forge.openautomation.common

import net.minecraft.item.ItemStack

object Util {
	def itemData(item: ItemStack) = {
	  Map("displayName" -> item.getDisplayName(), "name" -> item.getUnlocalizedName(), "damage" -> item.getItemDamage().asInstanceOf[Integer]).asInstanceOf[java.util.Map[AnyRef, AnyRef]]
	}
}