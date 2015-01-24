package com.awesomesauce.minecraft.forge.openautomation.common.oc.baubles.item

import baubles.api.{BaubleType, IBauble}
import li.cil.oc.api.machine.MachineHost
import li.cil.oc.api.network.Node
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.{Item, ItemStack}


class ItemMachineBauble(bType: BaubleType) extends Item with IBauble {
  val hostMap = scala.collection.mutable.Map[Int, MachineBaubleHost]()

  override def getBaubleType(stack: ItemStack) = bType

  override def onWornTick(stack: ItemStack, player: EntityLivingBase) = {
    val host = hostMap(stack.getTagCompound.getInteger("id"))
    host.machine.update()
  }

  override def onEquipped(stack: ItemStack, player: EntityLivingBase) = {
    val host = new MachineBaubleHost(stack, player)
    hostMap.put(stack.getTagCompound.getInteger("id"), host)
    host.machine.start()
  }

  override def onUnequipped(stack: ItemStack, player: EntityLivingBase) = {
    val host = hostMap(stack.getTagCompound.getInteger("id"))
    host.machine.stop()
    hostMap.remove(stack.getTagCompound.getInteger("id"))
  }

  override def canEquip(stack: ItemStack, player: EntityLivingBase) = true

  override def canUnequip(stack: ItemStack, player: EntityLivingBase) = true

  class MachineBaubleHost(stack: ItemStack, player: EntityLivingBase) extends MachineHost {
    val machine = li.cil.oc.api.Machine.create(this)

    def maxComponents = 0

    //TODO
    def markForSaving() = {}

    //TODO
    def callBudget = 0

    //TODO
    def world = player.worldObj

    def installedMemory = 0

    //TODO
    def xPosition = player.posX

    def yPosition = player.posY

    def zPosition = player.posZ

    def markChanged() = {}

    //TODO
    def onMachineDisconnect(node: Node) = {}

    //TODO
    def onMachineConnect(node: Node) = {}

    //TODO
    def cpuArchitecture = null

    //TODO
    def componentSlot(address: String) = 0 //TODO
  }

}
