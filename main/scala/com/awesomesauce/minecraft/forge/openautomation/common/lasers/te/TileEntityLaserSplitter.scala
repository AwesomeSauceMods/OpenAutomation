package com.awesomesauce.minecraft.forge.openautomation.common.lasers.te

import com.awesomesauce.minecraft.forge.core.lib.util.PlayerUtil
import com.awesomesauce.minecraft.forge.openautomation.api.lasers.{LaserPacket, LaserReciever}
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.LaserHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection


class TileEntityLaserSplitter extends TileEntity with LaserReciever {
  var dir1: ForgeDirection = ForgeDirection.UP
  var dir2: ForgeDirection = ForgeDirection.EAST

  def activate(player: EntityPlayer, side: Int, partx: Float, party: Float, partz: Float): Boolean = {
    if (!player.isSneaking) {
      if (dir1 == ForgeDirection.getOrientation(side)) {
        dir2 = dir2.getRotation(dir1)
      }
      else {
        dir1 = ForgeDirection.getOrientation(side)
        dir2 = dir2.getRotation(dir1)
      }
      PlayerUtil.sendChatMessage(player, "Receiving on side: " + dir1.toString)
      PlayerUtil.sendChatMessage(player, "Rotated to side: " + dir2.toString + " and: " + dir2.getOpposite.toString)
      true
    }
    else false
  }

  def arrive(from: ForgeDirection, packet: LaserPacket) = {
    var direction: ForgeDirection = from.getOpposite
    if (from == dir1) {
      direction = dir2
    }
    val splitPacket = packet.split(2)
    LaserHelper.sendLaser(worldObj, xCoord, yCoord, zCoord, direction, splitPacket(0))
    LaserHelper.sendLaser(worldObj, xCoord, yCoord, zCoord, direction.getOpposite, splitPacket(1))
  }
}
