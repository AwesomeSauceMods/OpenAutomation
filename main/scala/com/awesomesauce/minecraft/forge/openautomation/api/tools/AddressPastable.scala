package com.awesomesauce.minecraft.forge.openautomation.api.tools

/**
 * Able to use the Address Copier as a destination.
 */
trait AddressPastable {
  def pasteAddress(address:String)
}