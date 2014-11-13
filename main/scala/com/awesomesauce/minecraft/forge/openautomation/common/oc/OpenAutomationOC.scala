package com.awesomesauce.minecraft.forge.openautomation.common.oc

import com.awesomesauce.minecraft.forge.core.lib.item.ItemDescription
import com.awesomesauce.minecraft.forge.core.lib.util.ItemUtil
import com.awesomesauce.minecraft.forge.openautomation.common.OAModule
import com.awesomesauce.minecraft.forge.openautomation.common.item.ItemToolHead
import com.awesomesauce.minecraft.forge.openautomation.common.oc.item.ItemAddressCopier
import com.awesomesauce.minecraft.forge.openautomation.common.oc.oalang.DriverOAlangProcessor
import com.awesomesauce.minecraft.forge.openautomation.common.oc.te._
import li.cil.oc.api.{Driver, Items}
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.oredict.{ShapedOreRecipe, ShapelessOreRecipe}

object OpenAutomationOC extends OAModule {
  var itemIO: Block = null
  var itemAutoCore: Block = null
  var fluidIO: Block = null
  var workbench: Block = null

  var toolAddressCopier: Item = null

  var toolHeadAddressCopier: Item = null

  var pressureCrusher: Block = null
  var powerOutput: Block = null
  var elements: scala.collection.mutable.Map[Int, Item] = scala.collection.mutable.Map[Int, Item]()
  var oalangt1: Item = null
  var oalangt2: Item = null
  var oalangt3: Item = null

  def preInit() = {
    itemIO = ItemUtil.makeBlock(oa, "itemIO", Material.iron, () => new TileEntityItemIO, 2)
    fluidIO = ItemUtil.makeBlock(oa, "fluidIO", Material.iron, () => new TileEntityFluidIO, 2)
    itemAutoCore = ItemUtil.makeBlock(oa, "itemAutoCore", Material.iron, () => new TileEntityItemAutoCore)
    workbench = ItemUtil.makeBlock(oa, "workbench", Material.iron, () => new TileEntityWorkbench)
    toolAddressCopier = ItemUtil.makeItem(oa, "addressCopier", new ItemAddressCopier).setMaxStackSize(1).asInstanceOf[ItemDescription]
      .addUsage("awesomesauce.rightclick", "openautomation.tools.addressCopier.usage")
      .addUsage("awesomesauce.shiftrightclick", "openautomation.tools.disassemble.usage").indev
    toolHeadAddressCopier = ItemUtil.makeItem(oa, "toolHeadAddressCopier", new ItemToolHead(toolAddressCopier)).asInstanceOf[ItemDescription].addDescriptionLine("openautomation.tools.head.desc").indev
    pressureCrusher = ItemUtil.makeBlock(oa, "pressureCrusher", Material.iron, () => new TileEntityPressureCrusher)
    powerOutput = ItemUtil.makeBlock(oa, "powerOutput", Material.iron, () => new TileEntityPowerOutput)
    if (oa.config.getBoolean("enableChemistry", Configuration.CATEGORY_GENERAL, false, "Enable the chemistry component. NON COMPLETE")) {
      for (i <- Range(1, 83)) {
        elements.put(i, ItemUtil.makeItem(oa, "element" + i))
      }
    }
    oalangt1 = ItemUtil.makeItem(oa, "oalangt1")
    oalangt2 = ItemUtil.makeItem(oa, "oalangt2")
    oalangt3 = ItemUtil.makeItem(oa, "oalangt3")
    Driver.add(new DriverOAlangProcessor(new ItemStack(oalangt1), 16))
    Driver.add(new DriverOAlangProcessor(new ItemStack(oalangt2), 32))
    Driver.add(new DriverOAlangProcessor(new ItemStack(oalangt3), 64))
    ItemUtil.addRecipe(oa, new ShapedOreRecipe(new ItemStack(powerOutput),
      "xyx",
      "jad",
      "xmx",
      Character.valueOf('x'), "ingotIron", Character.valueOf('y'), "ingotGold",
      Character.valueOf('j'), Items.get("cable").createItemStack(1), Character.valueOf('a'), "ingotAwesomeite",
      Character.valueOf('d'), Items.get("printedCircuitBoard").createItemStack(1), Character.valueOf('m'), "oaOutputCode"))
  }

  def init() = {
    ItemUtil.addRecipe(oa, new ShapedOreRecipe(new ItemStack(itemIO), "xyx", "abc", "xzx",
      Character.valueOf('x'), "ingotAwesomeite", Character.valueOf('y'), Items.get("printedCircuitBoard").createItemStack(1),
      Character.valueOf('a'), "oaInputCode", Character.valueOf('b'), "oaItemCode", Character.valueOf('c'), "oaOutputCode",
      Character.valueOf('z'), "awesomeCore"))
    ItemUtil.addRecipe(oa, new ShapedOreRecipe(new ItemStack(fluidIO), "xyx", "abc", "xzx",
      Character.valueOf('x'), "ingotAwesomeite", Character.valueOf('y'), Items.get("printedCircuitBoard").createItemStack(1),
      Character.valueOf('a'), "oaInputCode", Character.valueOf('b'), "oaFluidCode", Character.valueOf('c'), "oaOutputCode",
      Character.valueOf('z'), "awesomeCore"))
    ItemUtil.addRecipe(oa, new ShapelessOreRecipe(new ItemStack(oa.codeBundle), Items.get("chip1").createItemStack(1), "blockAwesomeite"))
    ItemUtil.addRecipe(oa, new ShapelessOreRecipe(new ItemStack(oa.codeBundle), Items.get("chip2").createItemStack(1), "ingotAwesomeite", "ingotAwesomeite", "ingotAwesomeite"))
    ItemUtil.addRecipe(oa, new ShapelessOreRecipe(new ItemStack(oa.codeBundle), Items.get("chip3").createItemStack(1), "nuggetAwesomeite"))
  }

  def postInit() = {

  }
}
