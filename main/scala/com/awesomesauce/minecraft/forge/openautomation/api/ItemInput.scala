package com.awesomesauce.minecraft.forge.openautomation.api

import com.awesomesauce.minecraft.forge.core.lib.util.ReadOnlyInventory

/**
 * Able to be queried from the network to find an item.
 */
trait ItemInput {
  def inventory: ReadOnlyInventory
  def sendItem(slot : Int)
}