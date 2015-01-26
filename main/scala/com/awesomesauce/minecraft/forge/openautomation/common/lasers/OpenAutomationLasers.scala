package com.awesomesauce.minecraft.forge.openautomation.common.lasers

import com.awesomesauce.minecraft.forge.core.lib.item.Description
import com.awesomesauce.minecraft.forge.core.lib.util.ItemUtil
import com.awesomesauce.minecraft.forge.openautomation.api.lasers.{ILaserModule, LaserAPI, NullCallback}
import com.awesomesauce.minecraft.forge.openautomation.common.OAModule
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.callbacks._
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.te._
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.upgrades.{ItemLaserUpgradeEfficiency, ItemLaserUpgradePotato}
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.oredict.ShapedOreRecipe

object OpenAutomationLasers extends OAModule {
  LaserAPI.registerCallback(NullCallback)
  val name = "Lasers"

  var energyLaserEmitter: Block = null
  var playerLaserEmitter: Block = null
  var callbackLaserEmitter: Block = null

  var laserReceiver: Block = null
  var laserMirror: Block = null
  var laserSplitter: Block = null
  var laserUpgradeBase: Item = null
  var laserUpgradePotato: Item = null
  var laserUpgradeEfficiency: Item = null

  var energyLaserMultiple = 0.0
  var playerLaserCost = 0
  var playerLaserStorage = 0

  def preInit() = {
    energyLaserEmitter = ItemUtil.makeBlock(oa, "energyLaserEmitter", Material.iron, () => new TileEntityEnergyLaserEmitter)
    playerLaserEmitter = ItemUtil.makeBlock(oa, "playerLaserEmitter", Material.iron, () => new TileEntityPlayerLaserEmitter)
    playerLaserEmitter.asInstanceOf[Description].addUsage("awesomesauce.rightclick", "openautomation.playerLaserEmitter.usage.rightclick")
    //callbackLaserEmitter = ItemUtil.makeBlock(oa, "callbackLaserEmitter", Material.iron, () => new TileEntityCallbackLaserEmitter)
    laserReceiver = ItemUtil.makeBlock(oa, "laserReceiver", Material.iron, () => new TileEntityLaserReceiver)
    laserMirror = ItemUtil.makeBlock(oa, "laserMirror", Material.iron, () => new TileEntityLaserMirror)
    laserMirror.asInstanceOf[Description].addUsage("awesomesauce.rightclick", "openautomation.laserMirror.usage.rightclick")
    laserSplitter = ItemUtil.makeBlock(oa, "laserSplitter", Material.iron, () => new TileEntityLaserSplitter)
    laserSplitter.asInstanceOf[Description].addUsage("awesomesauce.rightclick", "openautomation.laserSplitter.usage.rightclick")
    laserUpgradePotato = ItemUtil.makeItem(oa, "laserUpgradePotato", new ItemLaserUpgradePotato)
    LaserAPI.registerLaserModule(laserUpgradePotato.asInstanceOf[ILaserModule])
    laserUpgradeEfficiency = ItemUtil.makeItem(oa, "laserUpgradeEfficiency", new ItemLaserUpgradeEfficiency)
    LaserAPI.registerLaserModule(laserUpgradeEfficiency.asInstanceOf[ILaserModule])


    LaserAPI.registerCallback(LaserMirrorRotateSide1)
    LaserAPI.registerCallback(LaserMirrorRotateSide2)
    LaserAPI.registerCallback(LaserCallbackChange)

    energyLaserMultiple = oa.config.get("lasers", "energyLaserMultiple", 0.001, "The energy loss of a laser, as a multiple to subtract.").getDouble
    playerLaserCost = oa.config.get("lasers", "playerLaserCost", 100000, "The cost for one operation of a player laser.").getInt
  }

  def init() = {
    ItemUtil.addRecipe(oa, new ShapedOreRecipe(new ItemStack(energyLaserEmitter),
      "iii", "abc", "idi",
      Character.valueOf('i'), "ingotIron", Character.valueOf('a'), "laserFocus",
      Character.valueOf('b'), "laserEmitter", Character.valueOf('c'), "dustRedstone",
      Character.valueOf('d'), "ingotAwesomeite"))
    ItemUtil.addRecipe(oa, new ShapedOreRecipe(new ItemStack(playerLaserEmitter), "iii", "abc", "idi",
      Character.valueOf('i'), "ingotIron", Character.valueOf('a'), "laserFocus",
      Character.valueOf('b'), "laserEmitter", Character.valueOf('c'), "gearEnderium",
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
