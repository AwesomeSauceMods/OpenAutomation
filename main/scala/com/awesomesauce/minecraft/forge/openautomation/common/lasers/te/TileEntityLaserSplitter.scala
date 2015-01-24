package com.awesomesauce.minecraft.forge.openautomation.common.lasers.te

import com.awesomesauce.minecraft.forge.core.lib.item.{BasicDismantleableTile, TActivatedTileEntity}
import com.awesomesauce.minecraft.forge.core.lib.util.PlayerUtil
import com.awesomesauce.minecraft.forge.openautomation.api.lasers.{LaserHelper, LaserMirror, LaserPacket}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection

class TileEntityLaserSplitter extends TileEntity with LaserMirror with TActivatedTileEntity with BasicDismantleableTile {
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
      markDirty()
      true
    }
    else false
  }

  def arrive(from: ForgeDirection, packet: LaserPacket): Boolean = {
    var direction: ForgeDirection = dir1
    if (from == dir1) {
      direction = dir2
      val splitPacket = packet.split(2)
      val res1 = LaserHelper.sendLaser(worldObj, xCoord, yCoord, zCoord, direction.getOpposite, splitPacket(1))
      val res2 = LaserHelper.sendLaser(worldObj, xCoord, yCoord, zCoord, direction, splitPacket(0))
      res1 || res2

    }
    else {
      LaserHelper.sendLaser(worldObj, xCoord, yCoord, zCoord, direction, packet)
    }
  }

  override def writeToNBT(tag: NBTTagCompound) = {
    super.writeToNBT(tag)
    tag.setString("dir1", dir1.toString)
    tag.setString("dir2", dir2.toString)
  }

  override def readFromNBT(tag: NBTTagCompound) = {
    super.readFromNBT(tag)
    dir1 = ForgeDirection.valueOf(tag.getString("dir1"))
    dir2 = ForgeDirection.valueOf(tag.getString("dir2"))
  }
}
