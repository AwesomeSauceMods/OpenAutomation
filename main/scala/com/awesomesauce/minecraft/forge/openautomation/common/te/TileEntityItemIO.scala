package com.awesomesauce.minecraft.forge.openautomation.common.te

import com.awesomesauce.minecraft.forge.core.lib.util.InventoryUtil
import com.awesomesauce.minecraft.forge.openautomation.api.ItemDestination
import li.cil.oc.api.Network
import li.cil.oc.api.network.Arguments
import li.cil.oc.api.network.Callback
import li.cil.oc.api.network.Context
import li.cil.oc.api.network.Visibility
import li.cil.oc.api.prefab.TileEntityEnvironment
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.ForgeDirection
import com.awesomesauce.minecraft.forge.openautomation.common.Util
import net.minecraft.nbt.NBTTagCompound
import com.awesomesauce.minecraft.forge.openautomation.api.tools.SideDefinable
import com.awesomesauce.minecraft.forge.openautomation.api.tools.AddressPastable
import com.awesomesauce.minecraft.forge.core.lib.util.ReadOnlyInventory
import com.awesomesauce.minecraft.forge.core.lib.util.InventoryWrapper
import li.cil.oc.api.network.Node
import com.awesomesauce.minecraft.forge.openautomation.api.ItemStorage

class TileEntityItemIO extends TileEntityEnvironment with ItemStorage with SideDefinable with AddressPastable {
  val node_ = Network.newNode(this, Visibility.Network).withComponent("itemIO").withConnector(200).create()
  node = node_
  var side: ForgeDirection = ForgeDirection.UNKNOWN
  var filter: String = "*"
  var address: String = "xxx"
  var slot: Int = -1
  //AddressPastable
  def pasteAddress(a: String) = address = a
  //SideDefinable
  def setSide(s: ForgeDirection) = side = s
  def inventoryy: IInventory = {
    val x = xCoord + side.offsetX
    val y = yCoord + side.offsetY
    val z = zCoord + side.offsetZ
    if (worldObj.getTileEntity(x, y, z).isInstanceOf[IInventory])
      return worldObj.getTileEntity(x, y, z).asInstanceOf[IInventory]
    else return null
  }
  //ItemStorage
  def inventory = new InventoryWrapper(inventoryy)
  def sendItem(i:Int) = {
    inventoryy.setInventorySlotContents(i, null)
  }
  def recieveItem(item: ItemStack): Boolean = InventoryUtil.addStackToSlotInInventory(inventoryy, item, slot)
  //Callbacks
  @Callback
  def ping(context: Context, arguments: Arguments): Array[AnyRef] = Array(this.node.address(), "pong")
  @Callback
  def setSide(context: Context, arguments: Arguments): Array[AnyRef] = {
    side = ForgeDirection.valueOf(arguments.checkString(0).toUpperCase())
    Array(true.asInstanceOf[java.lang.Boolean])
  }
  @Callback
  def getSide(context: Context, arguments: Arguments): Array[AnyRef] = {
    Array(side.toString())
  }
  @Callback
  def setFilter(context: Context, arguments: Arguments): Array[AnyRef] = {
    filter = arguments.checkString(0)
    Array(true.asInstanceOf[java.lang.Boolean])
  }
  @Callback
  def getFilter(context: Context, arguments: Arguments): Array[AnyRef] = {
    Array(filter)
  }
  @Callback
  def getInventoryName(context: Context, arguments: Arguments): Array[AnyRef] = {
    Array(inventory.getInventoryName())
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
  def setSlot(context: Context, arguments: Arguments): Array[AnyRef] = {
    slot = arguments.checkInteger(0)
    Array(true.asInstanceOf[java.lang.Boolean])
  }
  @Callback
  def getSlot(context: Context, arguments: Arguments): Array[AnyRef] = {
    Array(slot.asInstanceOf[Integer])
  }
  def doSendItem(stack: ItemStack, s: Int, destination: ItemDestination): Boolean = {
    if (destination.recieveItem(stack)) {
      inventoryy.setInventorySlotContents(s, null)
      return true
    }
    return false
  }
  @Callback
  def sendItem(context: Context, arguments: Arguments): Array[AnyRef] = {
    context.pause(0.5)
    val oldAddress = address
    val oldFilter = filter
    val oldSlot = slot
    if (arguments.isString(0))
      address = arguments.checkString(0)
    if (arguments.isString(0) && arguments.isString(1))
      filter = arguments.checkString(1)
    if (!arguments.isString(0) && arguments.isString(1))
      address = arguments.checkString(1)
    if (arguments.isString(2))
      filter = arguments.checkString(2)
    if (arguments.isInteger(0))
      slot = arguments.checkInteger(0)
    if (arguments.isInteger(1))
      slot = arguments.checkInteger(1)
    if (arguments.isInteger(2))
      slot = arguments.checkInteger(2)
    if (!node.network().node(address).host().isInstanceOf[ItemDestination])
      return Array(false.asInstanceOf[java.lang.Boolean])
    val destination = node.network().node(address).host().asInstanceOf[ItemDestination]
    if (slot != -1) return Array(doSendItem(inventory.getStackInSlot(slot), slot, destination).asInstanceOf[java.lang.Boolean])
    for (i <- Range(0, inventory.getSizeInventory())) {
      if (inventory.getStackInSlot(i) != null && doSendItem(inventory.getStackInSlot(i), i, destination)) {
        context.pause(0.5)
        node_.tryChangeBuffer(-20)
        address = oldAddress
        filter = oldFilter
        slot = oldSlot
        return Array(true.asInstanceOf[java.lang.Boolean])
      }
    }
    address = oldAddress
    filter = oldFilter
    slot = oldSlot
    Array(true.asInstanceOf[java.lang.Boolean])
  }
  @Callback
  def getItem(context: Context, arguments: Arguments): Array[AnyRef] = {
    val item = inventory.getStackInSlot(arguments.checkInteger(0))
    Array(Util.itemData(item))
  }
  //Save/Load
  override def readFromNBT(tag: NBTTagCompound) = {
    super.readFromNBT(tag)
    filter = tag.getString("filter")
    address = tag.getString("address")
    side = ForgeDirection.valueOf(tag.getString("side"))
    slot = tag.getInteger("slot")
  }
  override def writeToNBT(tag: NBTTagCompound) = {
    super.writeToNBT(tag)
    tag.setString("filter", filter)
    tag.setString("address", address)
    tag.setString("side", side.toString())
    tag.setInteger("slot", slot)
  }
}