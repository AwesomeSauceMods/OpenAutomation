package com.awesomesauce.minecraft.forge.openautomation.api.oa2

import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection

trait IOAStack {
  /**
   * Returns whether the stack matches the other stack. This is called on the least restrictive stack. (e.g. the filter, rather than the stack about what's there.)
   * @param other
   */
  def matchesUp(other: IOAStack): Boolean

  def isCompatibleWith(te: TileEntity): Boolean

  def pullStack(te: TileEntity, side: ForgeDirection): AnyRef

  def pushStack(te: TileEntity, side: ForgeDirection, stack: AnyRef): Boolean
}
