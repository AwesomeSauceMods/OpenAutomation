package com.awesomesauce.minecraft.forge.openautomation.common.te

import com.awesomesauce.minecraft.forge.core.lib.util.InventoryUtil
import com.awesomesauce.minecraft.forge.core.lib.util.ReadOnlyInventory
import com.awesomesauce.minecraft.forge.openautomation.api.ItemDestination
import com.awesomesauce.minecraft.forge.openautomation.api.tools.AddressPastable
import li.cil.oc.api.Network
import li.cil.oc.api.network.Arguments
import li.cil.oc.api.network.Callback
import li.cil.oc.api.network.Context
import li.cil.oc.api.network.Visibility
import li.cil.oc.api.prefab.TileEntityEnvironment
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagString

class TileEntityItemAutoCore extends TileEntityEnvironment with ItemDestination with AddressPastable {
  val node_ = Network.newNode(this, Visibility.Network).withComponent("itemAutoCore").withConnector(500).create()
  node = node_
  object inventory extends ReadOnlyInventory {
    override def getInventoryName(): String = "AutoItemCore"
    override def getInventoryStackLimit(): Int = 64
    override def getSizeInventory(): Int = {
      var num = 0
      for (inv <- inventories) {
        num += inv.inventory.getSizeInventory
      }
      return num
    }
    override def getStackInSlot(i: Int): ItemStack = {
      var num = i
      for (inv <- inventories) {
        if (num < inv.inventory.getSizeInventory)
          return inv.inventory.getStackInSlot(num)
        num -= inv.inventory.getSizeInventory
      }
      return null
    }
    override def hasCustomInventoryName(): Boolean = false
  }
  def pasteAddress(address: String): Unit = addAddress(address)
  def recieveItem(item: ItemStack): Boolean = sendItem(item)
  var inventories = scala.collection.mutable.Set[ItemDestination]()
  def addAddress(address: String) = {
    inventories.add(node.network().node(address).host().asInstanceOf[ItemDestination])
  }
  @Callback
  def addAddress(context: Context, arguments: Arguments): Array[AnyRef] = {
    addAddress(arguments.checkString(0))
    Array(true.asInstanceOf[java.lang.Boolean])
  }
  @Callback
  def resetAddresses(context: Context, arguments: Arguments): Array[AnyRef] = {
    inventories = scala.collection.mutable.Set[ItemDestination]()
    Array(true.asInstanceOf[java.lang.Boolean])
  }
  def sendItem(item: ItemStack): Boolean = {
    for (inv <- inventories) {
      if (InventoryUtil.scanInventoryForItems(inv.inventory, item) && inv.recieveItem(item)) {
        node_.changeBuffer(-10)
        return true
      }
    }
    return false
  }
  override def writeToNBT(tag: NBTTagCompound) = {
    super.writeToNBT(tag)
    val list = new NBTTagList()
    for (inv <- inventories)
      list.appendTag(new NBTTagString(inv.node().address()))
    tag.setTag("inventories", list)
  }
  override def readFromNBT(tag: NBTTagCompound) = {
    super.readFromNBT(tag)
    val list = tag.getTag("inventories").asInstanceOf[NBTTagList]
    for (i <- Range(0, list.tagCount())) {
      val address = list.getStringTagAt(i)
      inventories.add(node.network().node(address).host().asInstanceOf[ItemDestination])
    }
  }
}