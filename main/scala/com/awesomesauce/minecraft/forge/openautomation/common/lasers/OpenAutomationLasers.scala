package com.awesomesauce.minecraft.forge.openautomation.common.lasers

import com.awesomesauce.minecraft.forge.core.lib.util.ItemUtil
import com.awesomesauce.minecraft.forge.openautomation.common.OAModule
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.te.{TileEntityEnergyLaserEmitter, TileEntityLaserMirror, TileEntityLaserReceiver, TileEntityLaserSplitter}
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.oredict.ShapedOreRecipe


object OpenAutomationLasers extends OAModule {
  val name = "Lasers"

  var energyLaserEmitter: Block = null
  var laserReceiver: Block = null
  var laserMirror: Block = null
  var laserSplitter: Block = null
  var laserFocus: Item = null
  var laserEmitter: Item = null
  var laserReceptor: Item = null
  var laserMirrorCrafting: Item = null

  def preInit() = {
    energyLaserEmitter = ItemUtil.makeBlock(oa, "energyLaserEmitter", Material.iron, () => new TileEntityEnergyLaserEmitter)
    laserReceiver = ItemUtil.makeBlock(oa, "laserReceiver", Material.iron, () => new TileEntityLaserReceiver)
    laserMirror = ItemUtil.makeBlock(oa, "laserMirror", Material.iron, () => new TileEntityLaserMirror)
    laserSplitter = ItemUtil.makeBlock(oa, "laserSplitter", Material.iron, () => new TileEntityLaserSplitter)
    laserFocus = ItemUtil.makeItem(oa, "laserFocus", true)
    laserEmitter = ItemUtil.makeItem(oa, "laserEmitter", true)
    laserReceptor = ItemUtil.makeItem(oa, "laserReceptor", true)
    laserMirrorCrafting = ItemUtil.makeItem(oa, "laserMirror", true)
  }

  def init() = {
    ItemUtil.addRecipe(oa, new ShapedOreRecipe(new ItemStack(laserFocus, 2), "aba", " a ",
      Character.valueOf('b'), "blockGlass", Character.valueOf('a'), "nuggetAwesomeite"))
    ItemUtil.addRecipe(oa, new ShapedOreRecipe(new ItemStack(laserEmitter), "aaa", " ba", "  a",
      Character.valueOf('b'), "nuggetAwesomeite", Character.valueOf('a'), "dustGlowstone"))
    ItemUtil.addRecipe(oa, new ShapedOreRecipe(new ItemStack(laserReceptor), "aaa", " aa", "  a",
      Character.valueOf('a'), "nuggetAwesomeite"))
    ItemUtil.addRecipe(oa, new ShapedOreRecipe(new ItemStack(laserMirrorCrafting, 2), "aba", "aba", " c ",
      Character.valueOf('a'), "dustGlowstone", Character.valueOf('b'), "paneGlass", Character.valueOf('c'), "nuggetAwesomeite"))
    ItemUtil.addRecipe(oa, new ShapedOreRecipe(new ItemStack(energyLaserEmitter),
      "iii", "abc", "idi",
      Character.valueOf('i'), "ingotIron", Character.valueOf('a'), "laserFocus",
      Character.valueOf('b'), "laserEmitter", Character.valueOf('c'), "dustRedstone",
      Character.valueOf('d'), "ingotAwesomeite"))
    ItemUtil.addRecipe(oa, new ShapedOreRecipe(new ItemStack(laserReceiver),
      "iii", "abd", "idi",
      Character.valueOf('i'), "ingotIron", Character.valueOf('a'), "laserFocus",
      Character.valueOf('b'), "laserReceptor",
      Character.valueOf('d'), "ingotAwesomeite"))
    ItemUtil.addRecipe(oa, new ShapedOreRecipe(new ItemStack(laserMirror),
      "iai", "abi", "idi",
      Character.valueOf('i'), "ingotIron", Character.valueOf('a'), "laserFocus",
      Character.valueOf('b'), "laserMirror",
      Character.valueOf('d'), "ingotAwesomeite"))
    ItemUtil.addRecipe(oa, new ShapedOreRecipe(new ItemStack(laserSplitter),
      "iai", "aba", "idi",
      Character.valueOf('i'), "ingotIron", Character.valueOf('a'), "laserFocus",
      Character.valueOf('b'), "laserMirror",
      Character.valueOf('d'), "ingotAwesomeite"))
  }

  def postInit() = {

  }
}
