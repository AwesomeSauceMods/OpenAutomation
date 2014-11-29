package com.awesomesauce.minecraft.forge.openautomation.common.tconstruct

import com.awesomesauce.minecraft.forge.core.lib.util.ItemUtil
import com.awesomesauce.minecraft.forge.openautomation.common.OAModule
import com.awesomesauce.minecraft.forge.openautomation.common.tconstruct.te.TileEntityMelter
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.ShapedOreRecipe

object OpenAutomationTConstruct extends OAModule {
  val name = "TConstruct"
  var melter: Block = null
  var melterCost = 0
  var melterCostMultiplier = 0
  var melterMultiplier = 0

  def preInit() = {
    melterCost = oa.config.get("tconstruct", "melterCost", 60).getInt
    melterCostMultiplier = oa.config.get("tconstruct", "melterDivider", 10).getInt
    melterMultiplier = oa.config.get("tconstruct", "melterSpeed", 2).getInt
  }

  def init() = {
    melter = ItemUtil.makeBlock(oa, "tconstruct.melter", Material.rock, () => new TileEntityMelter)
    val materials = GameRegistry.findItem("TConstruct", "materials")
    val smeltery = GameRegistry.findBlock("TConstruct", "Smeltery")
    ItemUtil.addRecipe(oa, new ShapedOreRecipe(new ItemStack(melter), "xyx", "awa", "xzx",
      Character.valueOf('x'), new ItemStack(materials, 1, 2), Character.valueOf('y'), "ingotAwesomeite",
      Character.valueOf('z'), "awesomeCore", Character.valueOf('w'), new ItemStack(smeltery, 1, 0),
      Character.valueOf('a'), new ItemStack(smeltery, 1, 1)))
  }

  def postInit() = {

  }
}
