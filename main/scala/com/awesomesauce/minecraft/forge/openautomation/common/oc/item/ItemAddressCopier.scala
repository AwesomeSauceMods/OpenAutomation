package com.awesomesauce.minecraft.forge.openautomation.common.oc.item

import com.awesomesauce.minecraft.forge.openautomation.api.oc.tools.AddressPastable
import com.awesomesauce.minecraft.forge.openautomation.common.item.ItemTool
import com.awesomesauce.minecraft.forge.openautomation.common.oc.OpenAutomationOC
import li.cil.oc.api.network.Environment
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World

class ItemAddressCopier extends ItemTool {
  def toolhead = OpenAutomationOC.toolHeadAddressCopier

  override def onItemUse(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, xa: Float, ya: Float, za: Float): Boolean = {
    if (!stack.hasTagCompound()) {
      stack.setTagCompound(new NBTTagCompound())
      stack.getTagCompound().setString("address", "none")
    }
    if (world.getTileEntity(x, y, z).isInstanceOf[AddressPastable] && stack.getTagCompound().getString("address") != "null") {
      val te = world.getTileEntity(x, y, z).asInstanceOf[AddressPastable]
      te.pasteAddress(stack.getTagCompound().getString("address"))
      stack.getTagCompound().setString("address", "none")
    }
    else if (world.getTileEntity(x, y, z).isInstanceOf[Environment]) {
      val te = world.getTileEntity(x, y, z).asInstanceOf[Environment]
      if (te.node() != null)
        stack.getTagCompound()
          .setString("address",
            te.node()
              .address())
      return true
    }
    return false
  }

  override def addInformation(stack: ItemStack,
                              par2EntityPlayer: EntityPlayer, list: java.util.List[_], par4: Boolean) {
    val l = list.asInstanceOf[java.util.List[String]]
    if (!stack.hasTagCompound()) {
      stack.setTagCompound(new NBTTagCompound())
      stack.getTagCompound().setString("address", "none")
    }
    l.add(stack.getTagCompound().getString("address"))
  }
}