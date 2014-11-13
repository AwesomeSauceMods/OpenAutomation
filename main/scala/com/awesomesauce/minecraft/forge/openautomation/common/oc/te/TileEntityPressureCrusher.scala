package com.awesomesauce.minecraft.forge.openautomation.common.oc.te

import com.awesomesauce.minecraft.forge.core.components.AwesomeSauceComponents
import com.awesomesauce.minecraft.forge.core.lib.item.TActivatedTileEntity
import com.awesomesauce.minecraft.forge.core.lib.util.{InventoryWrapper, PlayerUtil}
import com.awesomesauce.minecraft.forge.openautomation.api.oc.tools.AddressPastable
import com.awesomesauce.minecraft.forge.openautomation.api.{ItemDestination, ItemStorage}
import li.cil.oc.api.Network
import li.cil.oc.api.machine.{Arguments, Callback, Context}
import li.cil.oc.api.network.Visibility
import li.cil.oc.api.prefab.TileEntityEnvironment
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary

class TileEntityPressureCrusher extends TileEntityEnvironment with AddressPastable with ItemStorage with IInventory with TActivatedTileEntity {

  val node_ = Network.newNode(this, Visibility.Network).withComponent("pressureCrusher").withConnector(2000).create()
  node = node_
  val recipes = AwesomeSauceComponents.grinderRecipes
  var address: String = "xxx"
  var crushingSlot: ItemStack = null
  var crushedSlot: ItemStack = null
  var cooldown = 200

  override def updateEntity() = {
    super.updateEntity()
    if (cooldown > 0)
      cooldown -= 1
  }

  def pasteAddress(add: String) = address = add

  def activate(player: EntityPlayer, side: Int, partx: Float, party: Float, partz: Float): Boolean = {
    val results = crush()
    if (results(0) == null) {
      PlayerUtil.sendChatMessage(player, results(1).toString)
    }
    true
  }

  def crush(): Array[AnyRef] = {
    if (cooldown > 0) {
      return Array(null, "Cooldown not empty. " + cooldown / 20 + "seconds remaining.")
    }
    for (oid <- OreDictionary.getOreIDs(crushingSlot)) {
      val ore = OreDictionary.getOreName(oid)
      if (recipes(ore) != null) {
        if (node_.tryChangeBuffer(-4000)) {
          if (crushedSlot == null)
            crushedSlot = recipes(ore).copy()
          else if (crushedSlot.getItem == recipes(ore).getItem)
            crushedSlot.stackSize += 2
          else
            return Array(null, "Target stack full")
          cooldown = 200
          return Array(true.asInstanceOf[java.lang.Boolean])
        }
        return Array(null, "Not enough power")
      }
    }
    Array(null, "Not Crushable Ore.")
  }

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
    context.pause(5)
    crush()
  }

  @Callback
  def sendItem(context: Context, arguments: Arguments): Array[AnyRef] = {
    context.pause(0.5)
    val oldAddress = address
    if (arguments.isString(0))
      address = arguments.checkString(0)
    if (!node.network().node(address).host().isInstanceOf[ItemDestination])
      return Array(null, "Not a valid destination.")
    val destination = node.network().node(address).host().asInstanceOf[ItemDestination]
    if (!node_.tryChangeBuffer(-20))
      return Array(null, "Not enough power.")
    Array({
      val res = doSendItem(this.getStackInSlot(0), 0, destination)
      address = oldAddress
      res
    }.asInstanceOf[java.lang.Boolean])
  }

  def doSendItem(stack: ItemStack, s: Int, destination: ItemDestination): Boolean = {
    if (destination.recieveItem(stack)) {
      this.setInventorySlotContents(s, null)
      return true
    }
    false
  }

  def setInventorySlotContents(i: Int, stack: net.minecraft.item.ItemStack): Unit = if (i == 1) crushingSlot = stack else crushedSlot = stack

  def getStackInSlot(i: Int): net.minecraft.item.ItemStack = if (i == 0) crushedSlot else crushingSlot

  def closeInventory(): Unit = {}

  def decrStackSize(i: Int, amount: Int): net.minecraft.item.ItemStack = if (i == 0) {
    crushedSlot.splitStack(amount)
  } else crushingSlot.splitStack(amount)

  def getInventoryName: String = "Pressure Crusher"

  def getInventoryStackLimit: Int = 64

  def getSizeInventory: Int = 2

  def getStackInSlotOnClosing(i: Int): net.minecraft.item.ItemStack = null

  def hasCustomInventoryName: Boolean = false

  def isItemValidForSlot(i: Int, stack: net.minecraft.item.ItemStack): Boolean = if (i == 0) false else true

  def isUseableByPlayer(player: net.minecraft.entity.player.EntityPlayer): Boolean = true

  def openInventory() = {}

  def recieveItem(item: net.minecraft.item.ItemStack): Boolean =
    if (crushingSlot == null) {
      crushingSlot = item
      true
    } else if (item.isItemEqual(crushingSlot)) {
      crushingSlot.stackSize += item.stackSize
      true
    } else false

  def inventory: com.awesomesauce.minecraft.forge.core.lib.util.ReadOnlyInventory = new InventoryWrapper(this)

  def sendItem(slot: Int) = if (slot == 0) crushedSlot = null else crushingSlot = null
}