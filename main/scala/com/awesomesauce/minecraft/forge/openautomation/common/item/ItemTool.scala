package com.awesomesauce.minecraft.forge.openautomation.common.item

import com.awesomesauce.minecraft.forge.core.lib.item.ItemDescription
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import com.awesomesauce.minecraft.forge.openautomation.common.OpenAutomation

abstract class ItemTool extends Item with ItemDescription {
  def toolhead:Item
  addUsage("awesomesauce.shiftrightclick", "openautomation.tools.disassemble.usage")
  override def onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack = {
    if (player.isSneaking()) {
      player.inventory.addItemStackToInventory(new ItemStack(toolhead))
      player.inventory.addItemStackToInventory(new ItemStack(OpenAutomation.toolBase))
      stack.stackSize -= 1
    }
    return stack
  }
}