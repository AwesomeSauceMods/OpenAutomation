package com.awesomesauce.minecraft.forge.openautomation.common.oc.baubles.item

import java.util

import baubles.api.{BaubleType, IBauble}
import cofh.api.energy.IEnergyContainerItem
import li.cil.oc.api.Driver
import li.cil.oc.api.machine.MachineHost
import li.cil.oc.api.network.{Connector, ManagedEnvironment, Node}
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.{NBTTagCompound, NBTTagList}
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
      inventory.add(stack)
      val driver = Driver.driverFor(stack, getClass)
      if (stack != null && driver != null) {
        val environment = driver.createEnvironment(stack, this)
        if (environment != null) {
          environment.load(driver.dataTag(stack))
        }
        components.add(environment)
        if (environment != null && environment.canUpdate) {
          updatingComponents.add(environment)
        }
      }
    }
    if (nbt.hasKey("machine")) {
      machine.load(nbt.getCompoundTag("machine"))
    }
  }

  def markChanged() = {
    val itemsNbt = new NBTTagList
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

class ItemMachineBauble(bType: BaubleType) extends Item with IBauble with IEnergyContainerItem {
  val hostMap = scala.collection.mutable.Map[Int, MachineBaubleHost]()

  def getEnergyStored(stack: ItemStack): Int = {
    val host = hostMap(stack.getTagCompound.getInteger("id"))
    (host.machine.node.asInstanceOf[Connector].globalBuffer() * 10).toInt
  }

  def extractEnergy(stack: ItemStack, maxExtract: Int, simulate: Boolean) = 0

  def receiveEnergy(stack: ItemStack, maxInsert: Int, simulate: Boolean) = {
    val host = hostMap(stack.getTagCompound.getInteger("id"))
    maxInsert - (host.machine.node.asInstanceOf[Connector].changeBuffer(maxInsert / 10) * 10).toInt
    host.markChanged()
  }

  def getMaxEnergyStored(stack: ItemStack): Int = {
    val host = hostMap(stack.getTagCompound.getInteger("id"))
    (host.machine.node.asInstanceOf[Connector].globalBufferSize() * 10).toInt
  }

  override def getBaubleType(stack: ItemStack) = bType

  override def onWornTick(stack: ItemStack, player: EntityLivingBase) = {
    if (!player.isClientWorld) {
      val host = hostMap(stack.getTagCompound.getInteger("id"))
      if (!host.machine.isRunning)
        onEquipped(stack, player)
      host.machine.update()
      for (i <- 0 until host.updatingComponents.size) {
        val environment = host.updatingComponents.get(i)
        environment.update()
      }
    }
  }

  override def onEquipped(stack: ItemStack, player: EntityLivingBase) = {
    if (!player.isClientWorld) {
      val host = new MachineBaubleHost(stack, player)
      hostMap.put(stack.getTagCompound.getInteger("id"), host)
      host.machine.start()
    }
  }

  override def onUnequipped(stack: ItemStack, player: EntityLivingBase) = {
    if (!player.isClientWorld) {
      val host = hostMap(stack.getTagCompound.getInteger("id"))
      host.machine.stop()
      for (i <- 0 until host.components.size) {
        val environment = host.components.get(i)
        if (environment != null) {
          environment.node.remove()
        }
      }
      host.markChanged()
      hostMap.remove(stack.getTagCompound.getInteger("id"))
    }
  }

  override def canEquip(stack: ItemStack, player: EntityLivingBase) = true

  override def canUnequip(stack: ItemStack, player: EntityLivingBase) = true


}
