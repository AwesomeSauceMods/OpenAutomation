package com.awesomesauce.minecraft.forge.openautomation.common.item

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import li.cil.oc.api.network.Environment
import com.awesomesauce.minecraft.forge.openautomation.api.tools.AddressPastable
import com.awesomesauce.minecraft.forge.openautomation.common.OpenAutomation
import net.minecraft.nbt.NBTTagCompound

class ItemAddressCopier extends ItemTool {
  def toolhead = OpenAutomation.toolHeadAddressCopier
  override def onItemUse(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, xa: Float, ya: Float, za: Float): Boolean = {
    if (!stack.hasTagCompound())
      stack.setTagCompound(new NBTTagCompound())
    if (world.getTileEntity(x, y, z).isInstanceOf[AddressPastable] && stack.getTagCompound().getString("address") != "null")
    {
      val te = world.getTileEntity(x, y, z).asInstanceOf[AddressPastable]
      te.pasteAddress(stack.getTagCompound().getString("address"))
      stack.getTagCompound().setString("address", "null")
    }
    if (world.getTileEntity(x, y, z).isInstanceOf[Environment])
    {
      val te = world.getTileEntity(x, y, z).asInstanceOf[Environment]
      stack.getTagCompound()
      .setString("address", 
          te.node()
          .address())
      return true
    }
    return false
  }
}