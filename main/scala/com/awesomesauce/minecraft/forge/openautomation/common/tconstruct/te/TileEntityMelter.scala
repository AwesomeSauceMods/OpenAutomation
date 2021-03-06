package com.awesomesauce.minecraft.forge.openautomation.common.tconstruct.te

import cofh.api.energy.{EnergyStorage, IEnergyReceiver}
import com.awesomesauce.minecraft.forge.openautomation.common.tconstruct.OpenAutomationTConstruct
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.{Fluid, FluidStack, FluidTank, IFluidHandler}
import tconstruct.library.crafting.Smeltery

class TileEntityMelter extends TileEntity with IInventory with IFluidHandler with IEnergyReceiver {
  val cost = OpenAutomationTConstruct.melterCost
  val multiplier = OpenAutomationTConstruct.melterMultiplier
  val costMultiplier = OpenAutomationTConstruct.melterCostMultiplier
  //END IInventory
  //BEGIN IFluidHandler
  val fluidTank = new FluidTank(10000)
  //END IFluidHandler
  //BEGIN IEnergyHandler
  val energyStorage = new EnergyStorage(10000)
  var temperatures = new Array[Int](3)
  //BEGIN IInventory
  var inventory = new Array[ItemStack](3)

  override def writeToNBT(tag: NBTTagCompound) = {
    super.writeToNBT(tag)
    val tankNBT = new NBTTagCompound
    fluidTank.writeToNBT(tankNBT)
    tag.setTag("tank", tankNBT)
    val energyNBT = new NBTTagCompound
    energyStorage.writeToNBT(energyNBT)
    tag.setTag("energy", energyNBT)
    tag.setIntArray("temperatures", temperatures)
  }

  override def readFromNBT(tag: NBTTagCompound) = {
    super.readFromNBT(tag)
    val tankNBT = tag.getCompoundTag("tank")
    fluidTank.readFromNBT(tankNBT)
    val energyNBT = tag.getCompoundTag("energy")
    energyStorage.readFromNBT(energyNBT)
    temperatures = tag.getIntArray("temperatures")
  }

  override def updateEntity() = {
    for (i <- Range(0, 3)) {
      if (Smeltery.getSmelteryResult(inventory(i)) != null) {
        val rfCost = cost + temperatures(i) / costMultiplier
        if (energyStorage.extractEnergy(rfCost, false) == rfCost) {
          energyStorage.extractEnergy(rfCost, true)
          temperatures(i) += multiplier
          if (temperatures(i) >= Smeltery.getLiquifyTemperature(inventory(i))) {
            val result = Smeltery.getSmelteryResult(inventory(i))
            if (fluidTank.fill(result, false) == result.amount) {
              fluidTank.fill(result, true)
              inventory(i) = null
              temperatures(i) = 0
            }
          }
        }
      }
    }
  }

  def decrStackSize(stack: Int, amount: Int) = {
    val olStack = inventory(stack)
    inventory(stack) = null
    olStack
  }

  def closeInventory() = {}

  def openInventory() = {}

  def getSizeInventory = 3

  def getInventoryStackLimit = 1

  def isItemValidForSlot(slot: Int, stack: ItemStack): Boolean = {
    Smeltery.getSmelteryResult(stack) != null
  }

  def getStackInSlotOnClosing(slot: Int) = null

  def setInventorySlotContents(slot: Int, stack: ItemStack) = {
    inventory(slot) = stack
  }

  def isUseableByPlayer(player: EntityPlayer) = true

  def getStackInSlot(slot: Int) = inventory(slot)

  def hasCustomInventoryName = false

  def getInventoryName = this.blockType.getUnlocalizedName

  def drain(from: ForgeDirection, stack: FluidStack, simulate: Boolean) = {
    if (stack.containsFluid(fluidTank.getFluid)) {
      fluidTank.drain(stack.amount, simulate)
    }
    else {
      null
    }
  }

  def drain(from: ForgeDirection, maxDrain: Int, simulate: Boolean) = {
    fluidTank.drain(maxDrain, simulate)
  }

  def fill(from: ForgeDirection, stack: FluidStack, simulate: Boolean) = 0

  def canFill(from: ForgeDirection, fluid: Fluid) = false

  def canDrain(from: ForgeDirection, fluid: Fluid) = true

  def getTankInfo(from: ForgeDirection) = Array(fluidTank.getInfo)

  def receiveEnergy(from: ForgeDirection, maxRecieve: Int, simulate: Boolean) = energyStorage.receiveEnergy(maxRecieve, simulate)

  def getEnergyStored(from: ForgeDirection) = {
    energyStorage.getEnergyStored
  }

  def getMaxEnergyStored(from: ForgeDirection) = energyStorage.getMaxEnergyStored

  def canConnectEnergy(from: ForgeDirection) = true

  //END IEnergyHandler
}
