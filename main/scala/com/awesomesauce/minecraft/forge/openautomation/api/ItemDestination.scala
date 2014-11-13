package com.awesomesauce.minecraft.forge.openautomation.api

import net.minecraft.item.ItemStack
/**
 * Able to recieve items from the network.
 */
trait ItemDestination extends ItemInventory {
	def recieveItem(item : ItemStack) : Boolean
}