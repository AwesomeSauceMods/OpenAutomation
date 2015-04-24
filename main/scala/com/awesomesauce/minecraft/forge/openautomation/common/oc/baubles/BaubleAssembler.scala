package com.awesomesauce.minecraft.forge.openautomation.common.oc.baubles

import com.awesomesauce.minecraft.forge.openautomation.common.oc.baubles.item.MachineBaubleHost
import cpw.mods.fml.common.event.FMLInterModComms
import li.cil.oc.api
import li.cil.oc.api.driver.EnvironmentHost
import li.cil.oc.api.driver.item.{Container, Memory, Processor}
import li.cil.oc.api.{Driver, Items}
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{NBTTagCompound, NBTTagList}
import net.minecraft.util.ChatComponentText


object BaubleAssembler {
  def validate(inv: IInventory): Array[AnyRef] = {
    var hasCPU = false
    var hasRAM = false
    var hasEEPROM = false
    for (i <- Range(0, inv.getSizeInventory)) {
      val item = inv.getStackInSlot(i)
      Driver.driverFor(item) match {
        case _: Memory => hasRAM = true
        case _: Processor => hasCPU = true
        case _ => //IDC
      }
      if (Items.get(item) == Items.get("eeprom"))
        hasEEPROM = true
    }
    if (!hasCPU) {
      return Array(java.lang.Boolean.FALSE, new ChatComponentText("Requires CPU."))
    }
    else if (!hasRAM) {
      return Array(java.lang.Boolean.FALSE, new ChatComponentText("Requires RAM."))
    }
    else if (!hasEEPROM) {
      return Array(java.lang.Boolean.FALSE, new ChatComponentText("Requires EEPROM"))
    }
    else if (complexity(inv) < maxComplexity(inv)) {
      return Array(java.lang.Boolean.TRUE, new ChatComponentText(complexity(inv) + "/" + maxComplexity(inv) + " Complexity."))
    }
    else {
      return Array(java.lang.Boolean.FALSE, new ChatComponentText(complexity(inv) + "/" + maxComplexity(inv) + " Complexity."))
    }
    Array[AnyRef](java.lang.Boolean.FALSE)
  }

  protected def complexity(inventory: IInventory) = {
    var acc = 0
    for (slot <- 1 until inventory.getSizeInventory) {
      val stack = inventory.getStackInSlot(slot)
      acc += (Option(api.Driver.driverFor(stack, hostClass)) match {
        case Some(driver: Processor) => 0 // CPUs are exempt, since they control the limit.
        case Some(driver: Container) => (1 + driver.tier(stack)) * 2
        case Some(driver) if driver.slot(stack) != "eeprom" => 1 + driver.tier(stack)
        case _ => 0
      })
    }
    acc
  }

  protected def hostClass: Class[_ <: EnvironmentHost] = classOf[MachineBaubleHost]

  def maxComplexity(inv: IInventory): Int = {
    if (selectRing(inv.getStackInSlot(0)))
      6
    else if (selectAmulet(inv.getStackInSlot(0)))
      10
    else if (selectBelt(inv.getStackInSlot(0)))
      20
    else
      0
  }

  def assemble(inventory: IInventory): Array[AnyRef] = {
    var stack: ItemStack = null
    if (selectRing(inventory.getStackInSlot(0))) {
      println("Assembling Ring.")
      stack = new ItemStack(OpenAutomationOCBaubles.baubleRing)
    }
    else if (selectAmulet(inventory.getStackInSlot(0))) {
      println("Assembling Amulet.")
      stack = new ItemStack(OpenAutomationOCBaubles.baubleAmulet)
    }
    else if (selectBelt(inventory.getStackInSlot(0))) {
      println("Assembling Belt.")
      stack = new ItemStack(OpenAutomationOCBaubles.baubleBelt)
    }
    else {
      println("WHAT THE FUUCK?")
      return Array[AnyRef](null, Integer.valueOf(0))
    }
    val items = new NBTTagList
    for (i <- 1 until inventory.getSizeInventory) {
      if (inventory.getStackInSlot(i) != null) {
        val tag = new NBTTagCompound
        inventory.getStackInSlot(i).writeToNBT(tag)
        items.appendTag(tag)
      }
    }
    val nbt = new NBTTagCompound
    nbt.setTag("items", items)
    stack.setTagCompound(nbt)
    Array[AnyRef](stack, java.lang.Double.valueOf(500))
  }

  def selectRing(stack: ItemStack) = stack.getItem == OpenAutomationOCBaubles.baubleRingBase

  def selectAmulet(stack: ItemStack) = stack.getItem == OpenAutomationOCBaubles.baubleAmuletBase

  def selectBelt(stack: ItemStack) = stack.getItem == OpenAutomationOCBaubles.baubleBeltBase

  def register() = {

    val upgradeSlot1 = new NBTTagCompound
    upgradeSlot1.setInteger("tier", 0)
    val upgradeSlot2 = new NBTTagCompound
    upgradeSlot2.setInteger("tier", 1)
    val upgradeSlot3 = new NBTTagCompound
    upgradeSlot3.setInteger("tier", 2)
    val componentSlotCard1 = new NBTTagCompound
    componentSlotCard1.setInteger("tier", 0)
    componentSlotCard1.setString("type", "card")
    val componentSlotCard2 = new NBTTagCompound
    componentSlotCard2.setInteger("tier", 1)
    componentSlotCard2.setString("type", "card")
    val componentSlotCPU2 = new NBTTagCompound
    componentSlotCPU2.setInteger("tier", 1)
    componentSlotCPU2.setString("type", "cpu")
    val componentSlotRAM2 = new NBTTagCompound
    componentSlotRAM2.setInteger("tier", 1)
    componentSlotRAM2.setString("type", "memory")
    val componentSlotEEPROM = new NBTTagCompound
    componentSlotEEPROM.setInteger("tier", 1)
    componentSlotEEPROM.setString("type", "eeprom")

    val ringTag = new NBTTagCompound()
    ringTag.setString("select", "com.awesomesauce.minecraft.forge.openautomation.common.oc.baubles.BaubleAssembler.selectRing")
    ringTag.setString("validate", "com.awesomesauce.minecraft.forge.openautomation.common.oc.baubles.BaubleAssembler.validate")
    ringTag.setString("assemble", "com.awesomesauce.minecraft.forge.openautomation.common.oc.baubles.BaubleAssembler.assemble")
    ringTag.setString("hostClass", "com.awesomesauce.minecraft.forge.openautomation.common.oc.baubles.item.MachineBaubleHost")
    var upgradeSlots = new NBTTagList
    upgradeSlots.appendTag(upgradeSlot1)
    upgradeSlots.appendTag(upgradeSlot1)
    upgradeSlots.appendTag(upgradeSlot1)
    ringTag.setTag("upgradeSlots", upgradeSlots)
    var componentSlots = new NBTTagList
    componentSlots.appendTag(new NBTTagCompound)
    componentSlots.appendTag(new NBTTagCompound)
    componentSlots.appendTag(new NBTTagCompound)
    componentSlots.appendTag(componentSlotCPU2)
    componentSlots.appendTag(componentSlotRAM2)
    componentSlots.appendTag(new NBTTagCompound)
    componentSlots.appendTag(componentSlotEEPROM)
    ringTag.setTag("componentSlots", componentSlots)
    FMLInterModComms.sendMessage("OpenComputers", "registerAssemblerTemplate", ringTag)

    val amuletTag = new NBTTagCompound()
    amuletTag.setString("select", "com.awesomesauce.minecraft.forge.openautomation.common.oc.baubles.BaubleAssembler.selectAmulet")
    amuletTag.setString("validate", "com.awesomesauce.minecraft.forge.openautomation.common.oc.baubles.BaubleAssembler.validate")
    amuletTag.setString("assemble", "com.awesomesauce.minecraft.forge.openautomation.common.oc.baubles.BaubleAssembler.assemble")
    amuletTag.setString("hostClass", "com.awesomesauce.minecraft.forge.openautomation.common.oc.baubles.item.MachineBaubleHost")
    upgradeSlots = new NBTTagList
    upgradeSlots.appendTag(upgradeSlot2)
    upgradeSlots.appendTag(upgradeSlot2)
    upgradeSlots.appendTag(upgradeSlot1)
    upgradeSlots.appendTag(upgradeSlot1)
    upgradeSlots.appendTag(upgradeSlot1)
    amuletTag.setTag("upgradeSlots", upgradeSlots)
    componentSlots = new NBTTagList
    componentSlots.appendTag(componentSlotCard2)
    componentSlots.appendTag(componentSlotCard1)
    componentSlots.appendTag(componentSlotCard1)
    componentSlots.appendTag(new NBTTagCompound)
    componentSlots.appendTag(componentSlotCPU2)
    componentSlots.appendTag(componentSlotRAM2)
    componentSlots.appendTag(new NBTTagCompound)
    componentSlots.appendTag(componentSlotEEPROM)
    amuletTag.setTag("componentSlots", componentSlots)
    FMLInterModComms.sendMessage("OpenComputers", "registerAssemblerTemplate", amuletTag)
  }
}
