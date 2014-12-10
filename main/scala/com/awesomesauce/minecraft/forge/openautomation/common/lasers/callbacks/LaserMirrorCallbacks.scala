package com.awesomesauce.minecraft.forge.openautomation.common.lasers.callbacks

import com.awesomesauce.minecraft.forge.openautomation.api.lasers.{LaserCallback, LaserMirror}
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

object LaserMirrorRotateSide1 extends LaserCallback {
  def getName = "Laser.Mirror.Rotate.Side1"

  def getDescription = "Rotate the first side of a mirror. Rotates to the opposite of the side the callback is executed to."

  def isUseableOn(destWorld: World, destX: Int, destY: Int, destZ: Int, destTo: ForgeDirection): Boolean = destWorld.getTileEntity(destX, destY, destZ).isInstanceOf[LaserMirror]

  def isExecutable(callbackEmitter: TileEntity, destWorld: World, destX: Int, destY: Int, destZ: Int, destTo: ForgeDirection): Boolean = true

  def executeCallback(callbackEmitter: TileEntity, destWorld: World, destX: Int, destY: Int, destZ: Int, destTo: ForgeDirection) = {
    val mirror = destWorld.getTileEntity(destX, destY, destZ).asInstanceOf[LaserMirror]
    mirror.dir1 = destTo
  }
}

object LaserMirrorRotateSide2 extends LaserCallback {
  def getName = "Laser.Mirror.Rotate.Side2"

  def getDescription = "Rotate the first side of a mirror. Rotates to the opposite of the side the callback is executed to."

  def isUseableOn(destWorld: World, destX: Int, destY: Int, destZ: Int, destTo: ForgeDirection): Boolean = destWorld.getTileEntity(destX, destY, destZ).isInstanceOf[LaserMirror]

  def isExecutable(callbackEmitter: TileEntity, destWorld: World, destX: Int, destY: Int, destZ: Int, destTo: ForgeDirection): Boolean = true

  def executeCallback(callbackEmitter: TileEntity, destWorld: World, destX: Int, destY: Int, destZ: Int, destTo: ForgeDirection) = {
    val mirror = destWorld.getTileEntity(destX, destY, destZ).asInstanceOf[LaserMirror]
    mirror.dir2 = destTo
  }
}

object LaserMirrorRotateHorizontal extends LaserCallback {
  def getName = "Laser.Mirror.Rotate.Horizontal"

  def getDescription = "Flips the mirror's reflection."

  def isUseableOn(destWorld: World, destX: Int, destY: Int, destZ: Int, destTo: ForgeDirection): Boolean = destWorld.getTileEntity(destX, destY, destZ).isInstanceOf[LaserMirror]

  def isExecutable(callbackEmitter: TileEntity, destWorld: World, destX: Int, destY: Int, destZ: Int, destTo: ForgeDirection): Boolean = true

  def executeCallback(callbackEmitter: TileEntity, destWorld: World, destX: Int, destY: Int, destZ: Int, destTo: ForgeDirection) = {
    val mirror = destWorld.getTileEntity(destX, destY, destZ).asInstanceOf[LaserMirror]
    mirror.dir2 = mirror.dir2.getOpposite
  }
}