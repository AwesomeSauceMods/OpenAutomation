package com.awesomesauce.minecraft.forge.openautomation.common.lasers.upgrades

import com.awesomesauce.minecraft.forge.core.lib.item.Description
import com.awesomesauce.minecraft.forge.openautomation.api.lasers.{ILaserModule, LaserPacket}
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.packets.EntityPacket
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{ItemFood, ItemStack}

class ItemLaserUpgradePotato extends ItemFood(4, 1.0F, false) with Description with ILaserModule {
  def stackIsModule(stack: ItemStack) = stack.getItem == this

  def modifyPacket(packet: LaserPacket) = {}

  def modifyPacketAfterArrival(packet: LaserPacket) = {
    if (packet.isInstanceOf[EntityPacket]) {
      val entity = packet.asInstanceOf[EntityPacket].entity
      if (entity.isInstanceOf[EntityPlayer]) {
        entity.asInstanceOf[EntityPlayer].getFoodStats.addStats(1, 0.5F)
      }
    }
  }
}
