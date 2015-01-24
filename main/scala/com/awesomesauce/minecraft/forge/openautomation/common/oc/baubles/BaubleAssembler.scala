package com.awesomesauce.minecraft.forge.openautomation.common.oc.baubles

import net.minecraft.item.ItemStack


object BaubleAssembler {
  def selectRing(stack: ItemStack) = stack.getItem == OpenAutomationOCBaubles.baubleRingBase

  def selectAmulet(stack: ItemStack) = stack.getItem == OpenAutomationOCBaubles.baubleAmuletBase

  def selectBelt(stack: ItemStack) = stack.getItem == OpenAutomationOCBaubles.baubleBeltBase
}
