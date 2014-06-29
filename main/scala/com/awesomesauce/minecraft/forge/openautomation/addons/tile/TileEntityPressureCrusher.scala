package com.awesomesauce.minecraft.forge.openautomation.addons.tile

import com.awesomesauce.minecraft.forge.core.components.AwesomeSauceComponents
import com.awesomesauce.minecraft.forge.core.lib.util.InventoryWrapper
import com.awesomesauce.minecraft.forge.openautomation.api.{ItemDestination, ItemStorage}
import com.awesomesauce.minecraft.forge.openautomation.api.tools.AddressPastable
import li.cil.oc.api.Network
import li.cil.oc.api.network.{Arguments, Callback, Context, Visibility}
import li.cil.oc.api.prefab.TileEntityEnvironment
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary

class TileEntityPressureCrusher extends TileEntityEnvironment with AddressPastable with ItemStorage with IInventory {
  val node_ = Network.newNode(this, Visibility.Network).withComponent("pressurecrusher").withConnector(1000).create()
  node = node_
  val recipes = AwesomeSauceComponents.grinderRecipes
  var address: String = "xxx"

  def pasteAddress(add: String) = address = add

  var crushingSlot: ItemStack = null
  var crushedSlot: ItemStack = null

  @Callback
  def setAddress(context: Context, arguments: Arguments): Array[AnyRef] = {
    address = arguments.checkString(0)
    Array(true.asInstanceOf[java.lang.Boolean])
  }

  @Callback
  def getAddress(context: Context, arguments: Arguments): Array[AnyRef] = {
    Array(address)
  }

  @Callback
  def crush(context: Context, arguments: Arguments): Array[AnyRef] = {
    for (oid <- OreDictionary.getOreIDs(crushingSlot)) {
      val ore = OreDictionary.getOreName(oid)
      if (recipes(ore) != null) {

        context.pause(5)
        if (node_.tryChangeBuffer(-100)) {
          if (crushedSlot == null)
            crushedSlot = recipes(ore).copy()
          else if (crushedSlot.getItem() == recipes(ore).getItem())
            crushedSlot.stackSize += 2
          else
            return Array(null, "Target stack full")
          return Array(true.asInstanceOf[java.lang.Boolean])
        }
        return Array(null, "Not enough power")
      }
    }
    return Array(null, "Not Crushable Ore.")
  }

  def doSendItem(stack: ItemStack, s: Int, destination: ItemDestination): Boolean = {
    if (destination.recieveItem(stack)) {
      this.setInventorySlotContents(s, null)
      return true
    }
    return false
  }

  @Callback
  def sendItem(context: Context, arguments: Arguments): Array[AnyRef] = {
    context.pause(0.5)
    val oldAddress = address
    if (arguments.isString(0))
      address = arguments.checkString(0)
    if (!node.network().node(address).host().isInstanceOf[ItemDestination])
      return Array(false.asInstanceOf[java.lang.Boolean])
    val destination = node.network().node(address).host().asInstanceOf[ItemDestination]
    return Array({
      val res = doSendItem(this.getStackInSlot(0), 0, destination)
      address = oldAddress
      res
    }.asInstanceOf[java.lang.Boolean])
    Array(true.asInstanceOf[java.lang.Boolean])
  }

  def closeInventory(): Unit = {}

  def decrStackSize(i: Int, amount: Int): net.minecraft.item.ItemStack = if (i == 0) {
    return crushedSlot.splitStack(amount)
  } else return crushingSlot.splitStack(amount)

  def getInventoryName(): String = "Pressure Crusher"

  def getInventoryStackLimit(): Int = 64

  def getSizeInventory(): Int = 2

  def getStackInSlot(i: Int): net.minecraft.item.ItemStack = if (i == 0) return crushedSlot else return crushingSlot

  def getStackInSlotOnClosing(i: Int): net.minecraft.item.ItemStack = null

  def hasCustomInventoryName(): Boolean = false

  def isItemValidForSlot(i: Int, stack: net.minecraft.item.ItemStack): Boolean = if (i == 0) return false else return true

  def isUseableByPlayer(player: net.minecraft.entity.player.EntityPlayer): Boolean = true

  def openInventory(): Unit = {}

  def setInventorySlotContents(i: Int, stack: net.minecraft.item.ItemStack): Unit = if (i == 1) crushingSlot = stack else crushedSlot = stack

  def recieveItem(item: net.minecraft.item.ItemStack): Boolean =
    if (crushingSlot == null) {
      crushingSlot = item
      return true
    } else if (item.isItemEqual(crushingSlot)) {
      crushingSlot.stackSize += item.stackSize
      return true
    } else return false

  def inventory: com.awesomesauce.minecraft.forge.core.lib.util.ReadOnlyInventory = new InventoryWrapper(this)

  def sendItem(slot: Int): Unit = if (slot == 0) crushedSlot = null else crushingSlot = null
}