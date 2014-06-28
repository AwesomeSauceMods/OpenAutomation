package com.awesomesauce.minecraft.forge.openautomation.api

import com.awesomesauce.minecraft.forge.core.lib.util.ReadOnlyInventory
import li.cil.oc.api.prefab.TileEntityEnvironment

/**
 * Able to be queried from the network to find an item.
 */
trait ItemInput extends TileEntityEnvironment with ItemInventory {
  def sendItem(slot : Int)
}