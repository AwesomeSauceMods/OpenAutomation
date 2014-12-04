package com.awesomesauce.minecraft.forge.openautomation.common.lasers.te

import com.awesomesauce.minecraft.forge.core.lib.item.{BasicDismantleableTile, TActivatedTileEntity}
import com.awesomesauce.minecraft.forge.core.lib.util.PlayerUtil
import com.awesomesauce.minecraft.forge.openautomation.api.lasers.{LaserPacket, LaserReciever}
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.LaserHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection

class TileEntityLaserMirror extends TileEntity with LaserReciever with TActivatedTileEntity with BasicDismantleableTile {
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
      PlayerUtil.sendChatMessage(player, "Rotating on side: " + dir1.toString)
      PlayerUtil.sendChatMessage(player, "Rotated to side: " + dir2.toString)
      true
    }
    else false
  }

  def arrive(from: ForgeDirection, packet: LaserPacket) = {
    var direction: ForgeDirection = from.getOpposite
    if (from == dir1) {
      direction = dir2

    }
    else if (from == dir2) {
      direction = dir1
    }
    else if (from == dir1.getOpposite) {
      direction = dir2.getOpposite
    }
    else if (from == dir2.getOpposite) {
      direction = dir1.getOpposite
    }
    LaserHelper.sendLaser(worldObj, xCoord, yCoord, zCoord, direction, packet)
  }

  override def writeToNBT(tag: NBTTagCompound) = {
    tag.setString("dir1", dir1.toString)
    tag.setString("dir2", dir2.toString)
  }

  override def readFromNBT(tag: NBTTagCompound) = {
    dir1 = ForgeDirection.valueOf(tag.getString("dir1"))
    dir2 = ForgeDirection.valueOf(tag.getString("dir2"))
  }
}
