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
import net.minecraftforge.oredict.{OreDictionary, ShapedOreRecipe, ShapelessOreRecipe}


object OpenAutomationPower extends OAModule {
  val name = "Power Generation"
  var laserEnergiser: Block = null
  var laserEnergiserXP: Block = null
  var laserEnergiserEmitter: Block = null
  var energisableRate = 200
  var fuelOutputModifier = 20
  var xpMaxUse = 30
  var xpModifier = 20

  def preInit() = {
    laserEnergiser = ItemUtil.makeBlock(oa, "laserEnergiser", Material.iron, () => new TileEntityEnergiserFuel)
    laserEnergiserXP = ItemUtil.makeBlock(oa, "laserEnergiserXP", Material.iron, () => new TileEntityEnergiserXP)
    laserEnergiserEmitter = ItemUtil.makeBlock(oa, "laserEnergisableEmitter", Material.iron, () => new TileEntityLaserEnergisableEmitter)
    energisableRate = oa.config.get(Configuration.CATEGORY_GENERAL, "power:energisableRate", 200, "Rate of sending energisable packets.").getInt
    fuelOutputModifier = oa.config.get(Configuration.CATEGORY_GENERAL, "power:fuelOutputModifier", 20, "Multiplier for fuel output in RF.").getInt
    xpMaxUse = oa.config.get(Configuration.CATEGORY_GENERAL, "power:xpMaxUse", 30, "The maximum amount of experience one energiser can drain.").getInt
    xpModifier = oa.config.get(Configuration.CATEGORY_GENERAL, "power:xpModifier", 20, "The multiplier for XP in RF.").getInt
  }

  def init() = {
    if (OreDictionary.getOres("ingotElectrum").size() > 0)
    ItemUtil.addRecipe(oa, new ShapelessOreRecipe(new ItemStack(laserEnergiserEmitter), OpenAutomationLasers.energyLaserEmitter, Blocks.redstone_block, "ingotElectrum"))
    else
      ItemUtil.addRecipe(oa, new ShapelessOreRecipe(new ItemStack(laserEnergiserEmitter), OpenAutomationLasers.energyLaserEmitter, Blocks.redstone_block, "ingotIron", "ingotGold"))


    ItemUtil.addRecipe(oa, new ShapedOreRecipe(new ItemStack(laserEnergiser), "xxx", "xyx", "yzy", Character.valueOf('x'), "dustRedstone", Character.valueOf('y'), "laserMirror", Character.valueOf('z'), Blocks.furnace))
  }

  def postInit() = {}
}
