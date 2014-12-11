package com.awesomesauce.minecraft.forge.openautomation.common.oc.callbacks

import com.awesomesauce.minecraft.forge.openautomation.api.lasers.LaserCallback
import li.cil.oc.api.internal.Case
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

object ComputerPowerTurnOn extends LaserCallback {
  def getName = "Computer.Case.Power.On"

  def getDescription = "Turn the computer on."

  def isUseableOn(destWorld: World, destX: Int, destY: Int, destZ: Int, destTo: ForgeDirection): Boolean = destWorld.getTileEntity(destX, destY, destZ).isInstanceOf[Case]

  def isExecutable(callbackEmitter: TileEntity, destWorld: World, destX: Int, destY: Int, destZ: Int, destTo: ForgeDirection): Boolean = !destWorld.getTileEntity(destX, destY, destZ).asInstanceOf[Case].machine().isRunning

  def executeCallback(callbackEmitter: TileEntity, destWorld: World, destX: Int, destY: Int, destZ: Int, destTo: ForgeDirection) = {
    val machine = destWorld.getTileEntity(destX, destY, destZ).asInstanceOf[Case].machine()
    machine.start()
  }
}

object ComputerPowerTurnOff extends LaserCallback {
  def getName = "Computer.Case.Power.Off"

  def getDescription = "Turn the computer off."

  def isUseableOn(destWorld: World, destX: Int, destY: Int, destZ: Int, destTo: ForgeDirection): Boolean = destWorld.getTileEntity(destX, destY, destZ).isInstanceOf[Case]

  def isExecutable(callbackEmitter: TileEntity, destWorld: World, destX: Int, destY: Int, destZ: Int, destTo: ForgeDirection): Boolean = destWorld.getTileEntity(destX, destY, destZ).asInstanceOf[Case].machine().isRunning

  def executeCallback(callbackEmitter: TileEntity, destWorld: World, destX: Int, destY: Int, destZ: Int, destTo: ForgeDirection) = {
    val machine = destWorld.getTileEntity(destX, destY, destZ).asInstanceOf[Case].machine()
    machine.stop()
  }
}