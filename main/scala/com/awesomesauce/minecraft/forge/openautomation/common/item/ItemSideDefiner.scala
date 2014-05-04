package com.awesomesauce.minecraft.forge.openautomation.common.item

import com.awesomesauce.minecraft.forge.core.lib.item.ItemDescription
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import com.awesomesauce.minecraft.forge.openautomation.api.tools.SideDefinable
import net.minecraftforge.common.util.ForgeDirection
import com.awesomesauce.minecraft.forge.openautomation.common.OpenAutomation

class ItemSideDefiner extends ItemTool {
  def toolhead = OpenAutomation.toolHeadSideDefiner
  override def onItemUse(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, xa: Float, ya: Float, za: Float): Boolean = {
    if (world.getTileEntity(x, y, z).isInstanceOf[SideDefinable])
    {
      val te = world.getTileEntity(x, y, z).asInstanceOf[SideDefinable]
      te.setSide(ForgeDirection.getOrientation(side).getOpposite())
      return true
    }
    return false
  }
}