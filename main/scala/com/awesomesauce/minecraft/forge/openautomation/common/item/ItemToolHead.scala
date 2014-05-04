package com.awesomesauce.minecraft.forge.openautomation.common.item

import com.awesomesauce.minecraft.forge.core.lib.item.ItemDescription
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import com.awesomesauce.minecraft.forge.openautomation.common.OpenAutomation

class ItemToolHead(tool: Item) extends ItemDescription {
  addUsage("awesomesauce.rightclick", "openautomation.toolhead.usage")
  override def onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack = {
    if (player.inventory.consumeInventoryItem(OpenAutomation.toolBase)) {
      player.inventory.addItemStackToInventory(new ItemStack(tool))
      stack.stackSize -= 1
    }
    return stack
  }
}