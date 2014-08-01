package com.awesomesauce.minecraft.forge.openautomation.common

import com.awesomesauce.minecraft.forge.openautomation.api.{FluidDestination, ItemDestination}
import li.cil.oc.api.network.Node
import net.minecraft.item.ItemStack

//import scala.collection.JavaConversions

object Util {
  def itemData(item: ItemStack): java.util.Map[AnyRef, AnyRef] = {
    Map("displayName" -> item.getDisplayName, "name" -> item.getUnlocalizedName, "damage" -> item.getItemDamage.asInstanceOf[Integer]).asInstanceOf[java.util.Map[AnyRef, AnyRef]]
  }

  def isItemDestination(node: Node, address: String) = node.network().node(address).host().isInstanceOf[ItemDestination]

  def getItemDestination(node: Node, address: String) = node.network().node(address).host().asInstanceOf[ItemDestination]

  def getFluidDestination(node: Node, address: String) = node.network().node(address).host().asInstanceOf[FluidDestination]

  def isFluidDestination(node: Node, address: String) = node.network().node(address).host().isInstanceOf[FluidDestination]
}