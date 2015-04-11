package com.awesomesauce.minecraft.forge.openautomation.common.power

import com.awesomesauce.minecraft.forge.core.lib.util.ItemUtil
import com.awesomesauce.minecraft.forge.openautomation.common.OAModule
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.OpenAutomationLasers
import com.awesomesauce.minecraft.forge.openautomation.common.power.te._
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.oredict.{ShapedOreRecipe, ShapelessOreRecipe}


object OpenAutomationPower extends OAModule {
  val name = "Power Generation"
  var laserEnergiser: Block = null
  var laserEnergiserEmitter: Block = null
  var energisableRate = 200
  var fuelOutputModifier = 20

  def preInit() = {
    laserEnergiser = ItemUtil.makeBlock(oa, "laserEnergiser", Material.iron, () => new TileEntityLaserEnergiser)
    laserEnergiserEmitter = ItemUtil.makeBlock(oa, "laserEnergisableEmitter", Material.iron, () => new TileEntityLaserEnergisableEmitter)
    energisableRate = oa.config.get(Configuration.CATEGORY_GENERAL, "power:energisableRate", 200, "Rate of sending energisable packets.").getInt
    fuelOutputModifier = oa.config.get(Configuration.CATEGORY_GENERAL, "power:fuelOutputModifier", 20, "Multiplier for fuel output in RF.").getInt
  }

  def init() = {
    ItemUtil.addRecipe(oa, new ShapelessOreRecipe(new ItemStack(laserEnergiserEmitter), OpenAutomationLasers.energyLaserEmitter, Blocks.redstone_block, "ingotElectrum"))
    ItemUtil.addRecipe(oa, new ShapedOreRecipe(new ItemStack(laserEnergiser), "xxx", "xyx", "yzy", 'x', "dustRedstone", 'y', "laserMirror", 'z', Blocks.furnace))
  }

  def postInit() = {}
}
