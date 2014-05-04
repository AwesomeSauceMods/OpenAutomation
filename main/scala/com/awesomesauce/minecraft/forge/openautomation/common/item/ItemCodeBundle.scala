package com.awesomesauce.minecraft.forge.openautomation.common.item

import com.awesomesauce.minecraft.forge.core.lib.item.ItemDescription
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import scala.util.Random
import com.awesomesauce.minecraft.forge.openautomation.common.OpenAutomation
import com.awesomesauce.minecraft.forge.core.lib.util.InventoryUtil

class ItemCodeBundle extends Item with ItemDescription {
  val codes: List[ItemStack] = List(new ItemStack(OpenAutomation.inputCode), new ItemStack(OpenAutomation.inputCode), new ItemStack(OpenAutomation.itemCode), new ItemStack(OpenAutomation.itemCode), new ItemStack(OpenAutomation.fluidCode), new ItemStack(OpenAutomation.outputCode), new ItemStack(OpenAutomation.outputCode))
  override def onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack = {
    System.out.println(codes(Random.nextInt(codes.size)).copy().getDisplayName())
    InventoryUtil.addStackToInventory(player.inventory, codes(Random.nextInt(codes.size)).copy())
    InventoryUtil.addStackToInventory(player.inventory, codes(Random.nextInt(codes.size)).copy())
    InventoryUtil.addStackToInventory(player.inventory, codes(Random.nextInt(codes.size)).copy())
    InventoryUtil.addStackToInventory(player.inventory, codes(Random.nextInt(codes.size)).copy())
    InventoryUtil.addStackToInventory(player.inventory, codes(Random.nextInt(codes.size)).copy())
    stack.stackSize -= 1
    return stack
  }
}