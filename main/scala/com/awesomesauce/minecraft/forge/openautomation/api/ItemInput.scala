package com.awesomesauce.minecraft.forge.openautomation.api

/**
 * Able to be queried from the network to find an item.
 */
trait ItemInput extends ItemInventory {
  def sendItem(slot: Int)
}