package com.awesomesauce.minecraft.forge.openautomation.common.power.te

import com.awesomesauce.minecraft.forge.openautomation.api.lasers.{LaserHelper, LaserPacket, LaserReciever}
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.packets.EnergyPacket
import com.awesomesauce.minecraft.forge.openautomation.common.power.OpenAutomationPower
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.{TileEntity, TileEntityFurnace}
import net.minecraftforge.common.util.ForgeDirection


class TileEntityLaserEnergiser extends TileEntity with LaserReciever with IInventory {
  var stack: ItemStack = null

  def decrStackSize(s: Int, a: Int) = null

  def closeInventory() = {}

  def openInventory() = {}

  def getSizeInventory = 1

  def getInventoryStackLimit = 1

  def arrive(from: ForgeDirection, laser: LaserPacket) = {
    if (laser.isInstanceOf[EnergyPacket] && laser.getCompound.hasKey("supportsEnergiser") && laser.getCompound.getBoolean("supportsEnergiser")) {
      if (laser.asInstanceOf[EnergyPacket].amount > TileEntityFurnace.getItemBurnTime(stack) * OpenAutomationPower.energisableRate * 2) {
        laser.asInstanceOf[EnergyPacket].amount += TileEntityFurnace.getItemBurnTime(stack) * OpenAutomationPower.energisableRate
        stack.stackSize -= 1
      }
    }
    LaserHelper.sendLaser(worldObj, xCoord, yCoord, zCoord, from.getOpposite, laser)
  }

  def isItemValidForSlot(s: Int, stack: ItemStack): Boolean = TileEntityFurnace.isItemFuel(stack)

  def getStackInSlotOnClosing(s: Int) = null

  def setInventorySlotContents(sl: Int, s: ItemStack) = stack = s

  def isUseableByPlayer(p: EntityPlayer) = true

  def getStackInSlot(s: Int) = stack

  def hasCustomInventoryName = false

  def getInventoryName = "LaserEnergiser"


}
