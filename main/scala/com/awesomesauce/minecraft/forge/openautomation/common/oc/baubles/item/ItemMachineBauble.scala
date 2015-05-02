package com.awesomesauce.minecraft.forge.openautomation.common.oc.baubles.item

import java.util

import baubles.api.{BaubleType, IBauble}
import cofh.api.energy.ItemEnergyContainer
import li.cil.oc.api.machine.MachineHost
import li.cil.oc.api.network.{Connector, ManagedEnvironment, Node}
import li.cil.oc.api.{Driver, Network}
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{NBTTagCompound, NBTTagList}
import net.minecraft.util.StatCollector
import net.minecraftforge.common.util.Constants


class MachineBaubleHost(stack: ItemStack, player: EntityLivingBase) extends MachineHost {
  lazy final val machine = li.cil.oc.api.Machine.create(this)
  val nbt = stack.getTagCompound
  val itemNBT = nbt.getTagList("items", Constants.NBT.TAG_COMPOUND)
  val inventory = new util.ArrayList[ItemStack](itemNBT.tagCount())
  val components = new util.ArrayList[ManagedEnvironment](inventory.size)
  val updatingComponents = new util.ArrayList[ManagedEnvironment](inventory.size)
  init()

  def init() = {
    for (i <- Range(0, itemNBT.tagCount())) {
      val in = i
      // Inventory loading.
      val stackNbt = itemNBT.getCompoundTagAt(in)
      val stack = ItemStack.loadItemStackFromNBT(stackNbt)
      println("Adding item: " + stack.getDisplayName)
      inventory.add(stack)
      createComponent(in, stack)
    }
    if (nbt.hasKey("machine")) {
      machine.load(nbt.getCompoundTag("machine"))
    }
    for (i <- 0 until components.size()) {
      val environment = components.get(i)
      Network.joinNewNetwork(machine.node)
      machine.node.connect(environment.node)
    }
  }

  def createComponent(slot:Int, stack:ItemStack) {
    // This check is just needed to make it simpler to use in onConnect.
    if (stack == null) {
      components.add(null); return
    }

    // Get the driver for the component, so we can create an environment.
    // You don't have to pass the host class here, but it's the preferred
    // way, since that allows for component blacklisting by host.
    val driver = Driver.driverFor(stack, getClass)
    if (driver != null) {
      val environment = driver.createEnvironment(stack, this)
      if (environment != null) {
        environment.load(driver.dataTag(stack))
        components.add(environment)
        if (components.get(components.size() - 1).canUpdate) {
          updatingComponents.add(components.get(components.size() - 1))
        }
      }
      else {
        components.add(null);
        return
      }
    }
    else {
      components.add(null);
      return
    }
  }

  def markChanged() = {
    val itemsNbt = new NBTTagList
    try {
      for (i <- 0 until inventory.size()) {
        val stack = inventory.get(i)

        // Save components to items, first, so the info gets saved with the items.
        val environment = components.get(i)
        val driver = Driver.driverFor(stack, getClass)
        if (stack != null && environment != null && driver != null) {
          environment.save(driver.dataTag(stack))
        }

        // Inventory saving.
        val stackNbt = new NBTTagCompound
        if (stack != null) {
          stack.writeToNBT(stackNbt)
        }
        itemsNbt.appendTag(stackNbt)
      }
    }
    catch {
      case e: IndexOutOfBoundsException =>
    }
    nbt.setTag("items", itemsNbt)
    val machineTag = new NBTTagCompound
    machine.save(machineTag)
    nbt.setTag("machine", machineTag)
  }

  def internalComponents = inventory

  def world = player.worldObj

  def xPosition = player.posX

  def yPosition = player.posY

  def zPosition = player.posZ

  def onMachineDisconnect(node: Node) = {}

  def onMachineConnect(node: Node) = {}

  def componentSlot(address: String): Int = {
    for (i <- Range(0, components.size())) {
      val environment = components.get(i)
      if (environment != null && environment.node() != null && environment.node().address().equals(address)) {
        return i
      }
    }
    -1
  }
}

class ItemMachineBauble(bType: BaubleType) extends ItemEnergyContainer(100000) with IBauble {
  val hosts = new java.util.ArrayList[MachineBaubleHost]()

  override def addInformation(stack: ItemStack, player: EntityPlayer, l: java.util.List[_], bool: Boolean) = {
    val list = l.asInstanceOf[java.util.List[Object]]
    if (stack.getTagCompound.hasKey("id")) {
      list.add("id" + stack.getTagCompound.getInteger("id"))
      val host = hosts.get(stack.getTagCompound.getInteger("id"))
    }
    list.add("" + getEnergyStored(stack) + "/" + getMaxEnergyStored(stack) + "RF")
    list.add("" + StatCollector.translateToLocal(stack.getTagCompound.getString("lastError")))
  }

  override def getBaubleType(stack: ItemStack) = bType

  override def onWornTick(stack: ItemStack, player: EntityLivingBase) = {
    try {
      if (!player.worldObj.isRemote) {
        val host = hosts.get(stack.getTagCompound.getInteger("id"))
        if (host.machine.node.asInstanceOf[Connector].localBuffer < 200) {
          host.machine.node.asInstanceOf[Connector].changeBuffer(extractEnergy(stack, 1000, false).toDouble / 10)
        }
        host.machine.update()
        for (i <- 0 until host.updatingComponents.size) {
          val environment = host.updatingComponents.get(i)
          environment.update()
        }
      }
    }
    catch {
      case e: IndexOutOfBoundsException => {
        e.printStackTrace()
        onEquipped(stack, player)
      }
    }
  }

  override def onEquipped(stack: ItemStack, player: EntityLivingBase) = {
    if (!player.worldObj.isRemote) {
      val host = new MachineBaubleHost(stack, player)
      stack.getTagCompound.setInteger("id", hosts.size)
      hosts.add(host)
      host.machine.node.asInstanceOf[Connector].setLocalBufferSize(1000)
      if (host.machine.node.asInstanceOf[Connector].localBuffer < 200) {
        host.machine.node.asInstanceOf[Connector].changeBuffer(extractEnergy(stack, 1000, false).toDouble / 10)
      }
      host.machine.start()
    }
  }

  override def onUnequipped(stack: ItemStack, player: EntityLivingBase) = {
    if (!player.worldObj.isRemote) {
      val host = hosts.get(stack.getTagCompound.getInteger("id"))
      host.machine.stop()
      for (i <- 0 until host.components.size) {
        val environment = host.components.get(i)
        if (environment != null) {
          environment.node.remove()
        }
      }
      host.markChanged()
      hosts.set(stack.getTagCompound.getInteger("id"), null)
      stack.getTagCompound.removeTag("id")
      if (host.machine.lastError() != null)
        stack.getTagCompound.setString("lastError", host.machine.lastError().toString)
    }
  }

  override def canEquip(stack: ItemStack, player: EntityLivingBase) = true

  override def canUnequip(stack: ItemStack, player: EntityLivingBase) = true


}
