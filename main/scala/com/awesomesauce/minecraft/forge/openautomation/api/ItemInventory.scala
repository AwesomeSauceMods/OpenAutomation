package com.awesomesauce.minecraft.forge.openautomation.api

import com.awesomesauce.minecraft.forge.core.lib.util.ReadOnlyInventory

trait ItemInventory {
	def inventory: ReadOnlyInventory
}