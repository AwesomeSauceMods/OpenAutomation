package com.awesomesauce.minecraft.forge.openautomation.common.oc.te

import com.awesomesauce.minecraft.forge.core.lib.util.{InventoryUtil, ReadOnlyInventory}
import com.awesomesauce.minecraft.forge.openautomation.api.oc.tools.AddressPastable
import com.awesomesauce.minecraft.forge.openautomation.api.{ItemDestination, ItemInput}
import li.cil.oc.api.Network
import li.cil.oc.api.machine.{Arguments, Callback, Context}
import li.cil.oc.api.network.Visibility
import li.cil.oc.api.prefab.TileEntityEnvironment
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{NBTTagCompound, NBTTagList, NBTTagString}

class TileEntityItemAutoCore extends TileEntityEnvironment with ItemDestination with AddressPastable {
  val node_ = Network.newNode(this, Visibility.Network).withComponent("itemAutoCore").withConnector(500).create()
  node = node_
  var inventories = scala.collection.mutable.Set[ItemDestination]()

  def pasteAddress(address: String): Unit = addAddress(address)

  def recieveItem(item: ItemStack): Boolean = sendItem(item)

  def sendItem(item: ItemStack): Boolean = {
    for (inv <- inventories) {
      if (InventoryUtil.scanInventoryForItems(inv.inventory, item) && inv.recieveItem(item)) {
        node_.changeBuffer(-100)
        return true
      }
    }
    false
  }

  @Callback
  def addAddress(context: Context, arguments: Arguments): Array[AnyRef] = {
    addAddress(arguments.checkString(0))
    Array(true.asInstanceOf[java.lang.Boolean])
  }

  def addAddress(address: String) = {
    inventories.add(node.network().node(address).host().asInstanceOf[ItemDestination])
  }

  @Callback
  def resetAddresses(context: Context, arguments: Arguments): Array[AnyRef] = {
    inventories = scala.collection.mutable.Set[ItemDestination]()
    Array(true.asInstanceOf[java.lang.Boolean])
  }

  @Callback
  def retrieveItem(context: Context, arguments: Arguments): Array[AnyRef] = {
    for (inv <- inventories) {
      if (inv.isInstanceOf[ItemInput]) {
        val inven = inv.inventory
        for (i <- Range(0, inv.inventory.getSizeInventory)) {
          if (inven.getStackInSlot(i) != null && inven.getStackInSlot(i).getItem.getUnlocalizedName.contains(arguments.checkString(0))) {
            val stack = inven.getStackInSlot(i)
            if (node.network().node(arguments.checkString(1)).host().asInstanceOf[ItemDestination].recieveItem(stack)) {
              inv.asInstanceOf[ItemInput].sendItem(i)
            }
          }
        }
      }
    }
    Array(true.asInstanceOf[java.lang.Boolean])
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

  object inventory extends ReadOnlyInventory {
    override def getInventoryName: String = "AutoItemCore"

    override def getInventoryStackLimit: Int = 64

    override def getSizeInventory: Int = {
      var num = 0
      for (inv <- inventories) {
        num += inv.inventory.getSizeInventory
      }
      num
    }

    override def getStackInSlot(i: Int): ItemStack = {
      var num = i
      for (inv <- inventories) {
        if (num < inv.inventory.getSizeInventory)
          return inv.inventory.getStackInSlot(num)
        num -= inv.inventory.getSizeInventory
      }
      null
    }

    override def hasCustomInventoryName: Boolean = false
  }

}