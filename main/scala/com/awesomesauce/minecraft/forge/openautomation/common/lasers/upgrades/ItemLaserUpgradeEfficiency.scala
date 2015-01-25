package com.awesomesauce.minecraft.forge.openautomation.common.lasers.upgrades

import com.awesomesauce.minecraft.forge.core.lib.item.ItemSimple
import com.awesomesauce.minecraft.forge.openautomation.api.lasers.{ILaserModule, LaserPacket}
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.packets.EnergyPacket
import net.minecraft.item.ItemStack

/**
 * Created by sandra on 1/25/15.
 */
class ItemLaserUpgradeEfficiency extends ItemSimple with ILaserModule {
  def stackIsModule(stack: ItemStack) = stack.getItem == this

  def modifyPacket(packet: LaserPacket) = {
    if (packet.isInstanceOf[EnergyPacket]) {
      val ePacket = packet.asInstanceOf[EnergyPacket]
      ePacket.amount = ePacket.origAmount
      ePacket.multiple = false
    }
  }

  def modifyPacketAfterArrival(packet: LaserPacket) = {}
}
