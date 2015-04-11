package com.awesomesauce.minecraft.forge.openautomation.common.power

import com.awesomesauce.minecraft.forge.core.lib.util.ItemUtil
import com.awesomesauce.minecraft.forge.openautomation.common.OAModule
import com.awesomesauce.minecraft.forge.openautomation.common.power.te._
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraftforge.common.config.Configuration


object OpenAutomationPower extends OAModule {
  val name = "Power Generation"
  var laserEnergiser: Block = null
  var laserEnergiserEmitter: Block = null
  var energisableRate = 200
  var fuelOutputModifier = 10

  def preInit() = {
    laserEnergiser = ItemUtil.makeBlock(oa, "laserEnergiser", Material.iron, () => new TileEntityLaserEnergiser)
    laserEnergiserEmitter = ItemUtil.makeBlock(oa, "laserEnergisableEmitter", Material.iron, () => new TileEntityLaserEnergisableEmitter)
    energisableRate = oa.config.get(Configuration.CATEGORY_GENERAL, "power:energisableRate", 200, "Rate of sending energisable packets.").getInt
    fuelOutputModifier = oa.config.get(Configuration.CATEGORY_GENERAL, "power:fuelOutputModifier", 10, "Multiplier for fuel output in RF.").getInt
  }

  def init() = {}

  def postInit() = {}
}
