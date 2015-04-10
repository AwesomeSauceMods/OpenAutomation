package com.awesomesauce.minecraft.forge.openautomation.common.power

import com.awesomesauce.minecraft.forge.core.lib.util.ItemUtil
import com.awesomesauce.minecraft.forge.openautomation.common.OAModule
import com.awesomesauce.minecraft.forge.openautomation.common.power.te._
import net.minecraft.block.Block
import net.minecraft.block.material.Material


class OpenAutomationPower extends OAModule {
  val name = "Power Generation"
  var laserEnergiser: Block = null
  var laserEnergiserEmitter: Block = null

  def preInit() = {
    laserEnergiser = ItemUtil.makeBlock(oa, "laserEnergiser", Material.iron, () => new TileEntityLaserEnergiser)
    laserEnergiserEmitter = ItemUtil.makeBlock(oa, "laserEnergisableEmitter", Material.iron, () => new TileEntityLaserEnergisableEmitter)
  }

  def init() = {}

  def postInit() = {}
}
