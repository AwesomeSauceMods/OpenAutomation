package com.awesomesauce.minecraft.forge.openautomation.common.lasers

import com.awesomesauce.minecraft.forge.core.lib.util.ItemUtil
import com.awesomesauce.minecraft.forge.openautomation.common.OAModule
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.te.{TileEntityEnergyLaserEmitter, TileEntityLaserMirror, TileEntityLaserReceiver}
import net.minecraft.block.Block
import net.minecraft.block.material.Material


object OpenAutomationLasers extends OAModule {
  val name = "Lasers"

  var energyLaserEmitter: Block = null
  var laserReceiver: Block = null
  var laserMirror: Block = null
  var laserSplitter: Block = null

  def preInit() = {
    energyLaserEmitter = ItemUtil.makeBlock(oa, "energyLaserEmitter", Material.iron, () => new TileEntityEnergyLaserEmitter)
    laserReceiver = ItemUtil.makeBlock(oa, "laserReceiver", Material.iron, () => new TileEntityLaserReceiver)
    laserMirror = ItemUtil.makeBlock(oa, "laserMirror", Material.iron, () => new TileEntityLaserMirror)
  }

  def init() = {

  }

  def postInit() = {

  }
}
