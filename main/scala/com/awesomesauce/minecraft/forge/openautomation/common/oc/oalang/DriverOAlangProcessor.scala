package com.awesomesauce.minecraft.forge.openautomation.common.oc.oalang

import li.cil.oc.api.driver.EnvironmentHost
import li.cil.oc.api.driver.item.{Processor, Slot}
import li.cil.oc.api.prefab.DriverItem
import net.minecraft.item.ItemStack

class DriverOAlangProcessor(item: ItemStack, components: Int) extends DriverItem(item) with Processor {
  val c = new OAlangArchitecture(null).getClass

  def slot(stack: ItemStack) = Slot.CPU

  def createEnvironment(stack: ItemStack, host: EnvironmentHost) = null

  def supportedComponents(stack: ItemStack) = if (stack.isItemEqual(item)) components else 0

  def architecture(stack: ItemStack) = if (stack.isItemEqual(item)) c else null
}
