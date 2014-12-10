package com.awesomesauce.minecraft.forge.openautomation.common.lasers.te

import cofh.api.energy.{EnergyStorage, IEnergyHandler}
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection

trait TEnergyReceiver extends TileEntity with IEnergyHandler {
  val energyStorageAmount: Int
  val energyCost: Int
  val energyStorage = new EnergyStorage(energyStorageAmount)
  println(energyStorage.getMaxEnergyStored)

  def extractEnergy(from: ForgeDirection, maxReceive: Int, simulate: Boolean) = 0

  def getEnergyStored(from: ForgeDirection) = energyStorage.getEnergyStored

  def getMaxEnergyStored(from: ForgeDirection) = energyStorage.getMaxEnergyStored

  def receiveEnergy(from: ForgeDirection, maxReceive: Int, simulate: Boolean) = energyStorage.receiveEnergy(maxReceive, simulate)

  def canConnectEnergy(from: ForgeDirection) = true

  def consumeEnergy(): Boolean = {
    if (energyStorage.extractEnergy(energyCost, true) == energyCost) {
      println("consuming: " + energyCost)
      energyStorage.extractEnergy(energyCost, false)
      true
    }
    else {
      println("not consuming: " + energyCost)
      false
    }
  }
}
