package com.awesomesauce.minecraft.forge.openautomation.api

import li.cil.oc.api.prefab.TileEntityEnvironment
import net.minecraft.item.ItemStack
import net.minecraft.inventory.IInventory
import com.awesomesauce.minecraft.forge.core.lib.util.ReadOnlyInventory
/**
 * Able to recieve items from the network.
 */
trait ItemDestination extends TileEntityEnvironment with ItemInventory {
	def recieveItem(item : ItemStack) : Boolean
}