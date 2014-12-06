package com.awesomesauce.minecraft.forge.openautomation.common.oc

import com.awesomesauce.minecraft.forge.core.lib.item.Description
import com.awesomesauce.minecraft.forge.core.lib.util.ItemUtil
import com.awesomesauce.minecraft.forge.openautomation.common.OAModule
import com.awesomesauce.minecraft.forge.openautomation.common.oc.driver.{DriverLaserMirror, EnvironmentBlockDriver}
import com.awesomesauce.minecraft.forge.openautomation.common.oc.oalang.DriverOAlangProcessor
import com.awesomesauce.minecraft.forge.openautomation.common.oc.te._
import li.cil.oc.api.{API, Driver, Items}
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.oredict.ShapedOreRecipe

object OpenAutomationOC extends OAModule {
  val name = "OC"
  var itemIO: Block = null
  var fluidIO: Block = null
  var workbench: Block = null

  var toolAddressCopier: Item = null

  var toolHeadAddressCopier: Item = null

  var pressureCrusher: Block = null
  var powerOutput: Block = null
  var oalangt1: Item = null
  var oalangt2: Item = null
  var oalangt3: Item = null
  var ocLaser: Block = null

  def preInit() = {
  }

  def init() = {
    powerOutput = ItemUtil.makeBlock(oa, "powerOutput", Material.iron, () => new TileEntityPowerOutput)
    powerOutput.asInstanceOf[Description].addDescriptionLine("Outputs RF at a rate of 1:10RF")
    ocLaser = ItemUtil.makeBlock(oa, "ocLaser", Material.iron, () => new TileEntityDataLaser)
    ocLaser.asInstanceOf[Description].addDescriptionLine("Emits a data laser containing arbitary").addDescriptionLine("data.")
    if (oa.config.get("modules", "oc.enableOALang", false, "Enable the OALang stuff.").getBoolean) {
      oalangt1 = ItemUtil.makeItem(oa, "oalangt1")
      oalangt2 = ItemUtil.makeItem(oa, "oalangt2")
      oalangt3 = ItemUtil.makeItem(oa, "oalangt3")
      Driver.add(new DriverOAlangProcessor(new ItemStack(oalangt1), 16))
      Driver.add(new DriverOAlangProcessor(new ItemStack(oalangt2), 32))
      Driver.add(new DriverOAlangProcessor(new ItemStack(oalangt3), 64))
    }
    ItemUtil.addRecipe(oa, new ShapedOreRecipe(new ItemStack(powerOutput),
      "xyx",
      "jad",
      "xmx",
      Character.valueOf('x'), "ingotIron", Character.valueOf('y'), "ingotGold",
      Character.valueOf('j'), Items.get("cable").createItemStack(1), Character.valueOf('a'), "ingotAwesomeite",
      Character.valueOf('d'), Items.get("printedCircuitBoard").createItemStack(1), Character.valueOf('m'), "dustRedstone"))
    API.driver.add(EnvironmentBlockDriver)
    API.driver.add(DriverLaserMirror)
  }

  def postInit() = {

  }
}
