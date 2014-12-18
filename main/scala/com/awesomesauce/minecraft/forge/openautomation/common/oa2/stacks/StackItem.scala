package com.awesomesauce.minecraft.forge.openautomation.common.oa2.stacks

import com.awesomesauce.minecraft.forge.core.lib.util.InventoryUtil
import com.awesomesauce.minecraft.forge.openautomation.api.oa2.{AbstractStack, IOAStack}
import cpw.mods.fml.common.registry.GameRegistry
import li.cil.oc.api.machine.{Arguments, Callback, Context}
import net.minecraft.inventory.{IInventory, ISidedInventory}
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection

import scala.util.control.Breaks._

class StackItem extends AbstractStack {
  var itemNameFilter = ""
  var maxMetaFilter = Integer.MAX_VALUE
  var minMetaFilter = Integer.MIN_VALUE
  var nbtFilter: NBTTagCompound = null

  def pullStack(te: TileEntity, side: ForgeDirection): AnyRef = {
    if (te.isInstanceOf[IInventory]) {
      val inv = te.asInstanceOf[IInventory]
      for (slot <- Range(0, inv.getSizeInventory)) {
        breakable {
          if (inv.isInstanceOf[ISidedInventory]) {
            val sinv = inv.asInstanceOf[ISidedInventory]
            if (!sinv.canExtractItem(slot, inv.getStackInSlot(slot), side.ordinal())) {
              break()
            }
          }
          if (matchesUp(new StackItem(inv.getStackInSlot(slot)))) {
            val stack = inv.getStackInSlot(slot)
            inv.setInventorySlotContents(slot, null)
            return stack
          }
        }
      }
    }
    null
  }

  def this(stack: ItemStack) = {
    this
    itemNameFilter = GameRegistry.findUniqueIdentifierFor(stack.getItem).toString
    maxMetaFilter = stack.getItemDamage
    minMetaFilter = stack.getItemDamage
    nbtFilter = stack.getTagCompound
  }

  def matchesUp(stack: IOAStack): Boolean = {
    if (stack.isInstanceOf[StackItem]) {
      val s = stack.asInstanceOf[StackItem]
      s.itemNameFilter.matches(itemNameFilter) && s.maxMetaFilter <= maxMetaFilter &&
        s.minMetaFilter >= minMetaFilter && (nbtFilter == null || nbtFilter.equals(s.nbtFilter))
    }
    else false
  }

  def pushStack(te: TileEntity, side: ForgeDirection, stack: AnyRef): Boolean = {
    val itemstack = stack.asInstanceOf[ItemStack]
    if (te.isInstanceOf[IInventory]) {
      val inv = te.asInstanceOf[IInventory]
      for (slot <- Range(0, inv.getSizeInventory)) {
        breakable {
          if (inv.isInstanceOf[ISidedInventory]) {
            val sinv = inv.asInstanceOf[ISidedInventory]
            if (!sinv.canInsertItem(slot, itemstack, side.ordinal())) {
              break()
            }
          }
          return InventoryUtil.addStackToSlotInInventory(inv, itemstack, slot)
        }
      }
    }
    false
  }

  @Callback
  def setItemNameFilter(context: Context, arguments: Arguments): Array[AnyRef] = {
    itemNameFilter = arguments.checkString(0)
    Array(java.lang.Boolean.TRUE)
  }

  @Callback
  def getItemNameFilter(context: Context, arguments: Arguments): Array[AnyRef] = {
    Array(itemNameFilter)
  }

  def load(tag: NBTTagCompound) = {

  }

  def save(tag: NBTTagCompound) = {

  }
}
