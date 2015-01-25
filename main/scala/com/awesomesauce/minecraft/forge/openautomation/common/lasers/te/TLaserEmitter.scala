package com.awesomesauce.minecraft.forge.openautomation.common.lasers.te

import com.awesomesauce.minecraft.forge.core.lib.item.TActivatedTileEntity
import com.awesomesauce.minecraft.forge.core.lib.util.PlayerUtil
import com.awesomesauce.minecraft.forge.openautomation.api.lasers.{ILaserModule, LaserAPI, LaserHelper, LaserPacket}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection

trait TLaserEmitter extends TileEntity with TActivatedTileEntity {
  val modules = scala.collection.mutable.Set[ILaserModule]()

  def sendLaser(side: ForgeDirection, packet: LaserPacket) = {
    for (module <- modules)
      module.modifyPacket(packet)
    if (LaserHelper.sendLaser(getWorldObj, xCoord, yCoord, zCoord, side, packet)) {
      for (module <- modules)
        module.modifyPacketAfterArrival(packet)
      true
    }
    else false
  }

  def activate(player: EntityPlayer, side: Int, partx: Float, party: Float, partz: Float): Boolean = {
    if (player.getHeldItem != null)
      if (LaserAPI.isModule(player.getHeldItem)) {
        modules.add(LaserAPI.getModule(player.getHeldItem))
        PlayerUtil.sendChatMessage(player, "Added module.")
        return true
      }
    false
  }
}
