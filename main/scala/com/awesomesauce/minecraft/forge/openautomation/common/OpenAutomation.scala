package com.awesomesauce.minecraft.forge.openautomation.common

import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import com.awesomesauce.minecraft.forge.core.lib.TAwesomeSauceMod
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.Mod.EventHandler
import com.awesomesauce.minecraft.forge.core.lib.TAwesomeSauceMod
import net.minecraft.block.Block
import com.awesomesauce.minecraft.forge.core.lib.util.ItemUtil
import net.minecraft.block.material.Material
import com.awesomesauce.minecraft.forge.openautomation.common.te.TileEntityItemIO
import com.awesomesauce.minecraft.forge.openautomation.common.item.ItemSideDefiner
import net.minecraft.item.Item
import com.awesomesauce.minecraft.forge.core.lib.item.ItemDescription
import com.awesomesauce.minecraft.forge.core.lib.item.ItemDescription
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraftforge.oredict.ShapedOreRecipe
import net.minecraft.item.ItemStack
import li.cil.oc.api.Items
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.common.ChestGenHooks
import net.minecraft.util.WeightedRandomChestContent
import com.awesomesauce.minecraft.forge.openautomation.common.item.ItemCodeBundle
import com.awesomesauce.minecraft.forge.openautomation.common.item.ItemToolHead
import net.minecraftforge.oredict.ShapelessOreRecipe
import com.awesomesauce.minecraft.forge.openautomation.common.item.ItemAddressCopier
import com.awesomesauce.minecraft.forge.openautomation.common.te.TileEntityItemAutoCore
import com.awesomesauce.minecraft.forge.openautomation.common.te.TileEntityWorkbench
import com.awesomesauce.minecraft.forge.openautomation.common.te.TileEntityFluidIO
import com.awesomesauce.minecraft.forge.openautomation.common.te.TileEntityPressureCrusher

@Mod(modid = OpenAutomation.MODID, name = OpenAutomation.MODNAME, modLanguage = "scala")
object OpenAutomation extends TAwesomeSauceMod {
  @EventHandler
  def aspri(e: FMLPreInitializationEvent) = super.awesomesaucepreinit(e)
  @EventHandler
  def asi(e: FMLInitializationEvent) = super.awesomesauceinit(e)
  @EventHandler
  def aspoi(e: FMLPostInitializationEvent) = super.awesomesaucepostinit(e)
  final val MODID = "OpenAutomation"
  final val MODNAME = "OpenAutomation"
  var itemIO: Block = null
  var pressureCrusher: Block = null
  var itemAutoCore: Block = null
  var fluidIO: Block = null
  var workbench: Block = null
  
  var toolSideDefiner: Item = null
  var toolAddressCopier: Item = null

  var toolBase: Item = null
  var toolHeadSideDefiner: Item = null
  var toolHeadAddressCopier: Item = null

  var codeBundle: Item = null

  var inputCode: Item = null
  var itemCode: Item = null
  var fluidCode: Item = null
  var outputCode: Item = null
  def getModID: String = MODID
  def getModName: String = MODNAME
  def getTabIconItem: () => net.minecraft.item.Item = () => codeBundle
  def getTextureDomain: String = "openautomation"
  def preInit() = {}
  def init() = {
    toolBase = ItemUtil.makeItem(this, "toolBase")
      .addDescriptionLine("openautomation.toolBase.desc").indev
    itemIO = ItemUtil.makeBlock(this, "itemIO", Material.iron, () => new TileEntityItemIO)
    pressureCrusher = ItemUtil.makeBlock(this, "pressureCrusher", Material.iron, () => new TileEntityPressureCrusher)
    fluidIO = ItemUtil.makeBlock(this, "fluidIO", Material.iron, () => new TileEntityFluidIO)
    itemAutoCore = ItemUtil.makeBlock(this, "itemAutoCore", Material.iron, () => new TileEntityItemAutoCore)
    workbench = ItemUtil.makeBlock(this, "workbench", Material.iron, () => new TileEntityWorkbench)
    toolSideDefiner = ItemUtil.makeItem(this, "sideDefiner", new ItemSideDefiner).asInstanceOf[ItemDescription]
      .addUsage("awesomesauce.rightclick", "openautomation.tools.sideDefiner.usage")
      .addUsage("awesomesauce.shiftrightclick", "openautomation.tools.disassemble.usage").indev
    toolHeadSideDefiner = ItemUtil.makeItem(this, "toolHeadSideDefiner", new ItemToolHead(toolSideDefiner)).asInstanceOf[ItemDescription].addDescriptionLine("openautomation.tools.head.desc").indev
    toolAddressCopier = ItemUtil.makeItem(this, "addressCopier", new ItemAddressCopier).setMaxStackSize(1).asInstanceOf[ItemDescription]
      .addUsage("awesomesauce.rightclick", "openautomation.tools.addressCopier.usage")
      .addUsage("awesomesauce.shiftrightclick", "openautomation.tools.disassemble.usage").indev
    toolHeadAddressCopier = ItemUtil.makeItem(this, "toolHeadAddressCopier", new ItemToolHead(toolAddressCopier)).asInstanceOf[ItemDescription].addDescriptionLine("openautomation.tools.head.desc").indev
    inputCode = ItemUtil.makeItem(this, "inputCode")
      .addDescriptionLine("openautomation.code.desc")
      .addDescriptionLine("openautomation.inputCode.desc")
    itemCode = ItemUtil.makeItem(this, "itemCode")
      .addDescriptionLine("openautomation.code.desc")
      .addDescriptionLine("openautomation.itemCode.desc")
    outputCode = ItemUtil.makeItem(this, "outputCode")
      .addDescriptionLine("openautomation.code.desc")
      .addDescriptionLine("openautomation.outputCode.desc")
    fluidCode = ItemUtil.makeItem(this, "fluidCode")
      .addDescriptionLine("openautomation.code.desc")
      .addDescriptionLine("openautomation.fluidCode.desc")
    codeBundle = ItemUtil.makeItem(this, "codeBundle", new ItemCodeBundle).asInstanceOf[ItemDescription]
      .addDescriptionLine("openautomation.codeBundle.desc")
      .addUsage("awesomesauce.rightclick", "openautomation.codeBundle.usage")
    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemIO), "xyx", "abc", "xyx",
      Character.valueOf('x'), "ingotAwesomeite", Character.valueOf('y'), Items.PrintedCircuitBoard,
      Character.valueOf('a'), inputCode, Character.valueOf('b'), itemCode, Character.valueOf('c'), outputCode))
    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(codeBundle),Items.MicrochipTier1, "blockAwesomeite"))
    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(codeBundle), Items.MicroChipTier2, "ingotAwesomeite", "ingotAwesomeite", "ingotAwesomeite"))
    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(codeBundle),Items.MicroChipTier3, "nuggetAwesomeite"))
  }
  def postInit() = {}
}