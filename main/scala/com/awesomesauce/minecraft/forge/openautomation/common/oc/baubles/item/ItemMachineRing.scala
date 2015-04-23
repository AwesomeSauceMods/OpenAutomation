package com.awesomesauce.minecraft.forge.openautomation.common.oc.baubles.item

import java.util

import baubles.api.{BaubleType, IBauble}
import li.cil.oc.api.Driver
import li.cil.oc.api.machine.MachineHost
import li.cil.oc.api.network.ManagedEnvironment
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.common.util.Constants


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
    lazy final val machine = li.cil.oc.api.Machine.create(this)
    val nbt = stack.getTagCompound
    val itemNBT = nbt.getTagList("items", Constants.NBT.TAG_COMPOUND)
    private val inventory = new Array[ItemStack](itemNBT.tagCount())
    private val components = new Array[ManagedEnvironment](inventory.length)
    private val updatingComponents = new util.ArrayList[ManagedEnvironment](inventory.length)
    init()

    def init() = {
      for (i <- Range(0, itemNBT.tagCount())) {
        val in = i
        // Inventory loading.
        val stackNbt = itemNBT.getCompoundTagAt(in)
        val stack = ItemStack.loadItemStackFromNBT(stackNbt)
        inventory(in) = stack
        val driver = Driver.driverFor(stack, getClass)
        if (stack != null && driver != null) {
          val environment = driver.createEnvironment(stack, this)
          if (environment != null) {
            environment.load(driver.dataTag(stack))
          }
          components(in) = environment
          if (environment != null && environment.canUpdate) {
            updatingComponents.add(environment)
          }
        }
      }
    }

    def markChanged() = {}

    def internalComponents = inventory
    def world = player.worldObj

    def xPosition = player.posX

    def yPosition = player.posY

    def zPosition = player.posZ
  }

}
