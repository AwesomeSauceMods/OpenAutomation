package com.awesomesauce.minecraft.forge.openautomation.common

import com.awesomesauce.minecraft.forge.core.lib.TAwesomeSauceMod
import com.awesomesauce.minecraft.forge.core.lib.item.ItemDescription
import com.awesomesauce.minecraft.forge.core.lib.util.ItemUtil
import com.awesomesauce.minecraft.forge.openautomation.common.item.{ItemAddressCopier, ItemCodeBundle, ItemSideDefiner, ItemToolHead}
import com.awesomesauce.minecraft.forge.openautomation.common.te.{TileEntityFluidIO, TileEntityItemAutoCore, TileEntityItemIO, TileEntityWorkbench}
import cpw.mods.fml.common.{Loader, Mod}
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import li.cil.oc.api.Items
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.oredict.{ShapedOreRecipe, ShapelessOreRecipe}

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
  var RnDSupport = false

  def getModID: String = MODID

  def getModName: String = MODNAME

  override def getVersion = "0.3.0"

  def getTabIconItem: () => net.minecraft.item.Item = () => codeBundle

  def getTextureDomain: String = "openautomation"

  def preInit() = {
    RnDSupport = config.get(Configuration.CATEGORY_GENERAL, "RnDTech Support", true).getBoolean(true) && Loader.isModLoaded("RnDTech")
  }

  def init() = {
    toolBase = ItemUtil.makeItem(this, "toolBase")
      .addDescriptionLine("openautomation.toolBase.desc").indev
    itemIO = ItemUtil.makeBlock(this, "itemIO", Material.iron, () => new TileEntityItemIO)
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
    /*if (RnDSupport) {
      RnDTechAPI.addResearch(new Research(new RnDRecipeBasic(
        new ItemStack(itemCode),
        Set(new ItemStack(AwesomeSauceComponents.ingotAwesomeite, 1),
          new ItemStack(Items.PrintedCircuitBoard.getItem(), 4), new ItemStack(inputCode), new ItemStack(itemCode, 2), new ItemStack(outputCode)),
        new ItemStack(RnDTech.researchDust, 2)), new RnDRecipeBasic(
            new ItemStack(itemCode),
            Set(new ItemStack(AwesomeSauceComponents.ingotAwesomeite, 3)), new ItemStack(itemIO)), "itemIO", "OA Item IO", "You have discovered how to digitize,\nand remake items through a computer."))
    } else {*/

    ItemUtil.addRecipe(this, new ShapedOreRecipe(new ItemStack(itemIO), "xyx", "abc", "xyx",
      Character.valueOf('x'), "ingotAwesomeite", Character.valueOf('y'), Items.get("printedCircuitBoard").item(),
      Character.valueOf('a'), inputCode, Character.valueOf('b'), itemCode, Character.valueOf('c'), outputCode))
    ItemUtil.addRecipe(this, new ShapelessOreRecipe(new ItemStack(codeBundle), Items.get("chip1").item(), "blockAwesomeite"))
    ItemUtil.addRecipe(this, new ShapelessOreRecipe(new ItemStack(codeBundle), Items.get("chip2").item(), "ingotAwesomeite", "ingotAwesomeite", "ingotAwesomeite"))
    ItemUtil.addRecipe(this, new ShapelessOreRecipe(new ItemStack(codeBundle), Items.get("chip3").item(), "nuggetAwesomeite"))
    //}
  }

  def postInit() = {}
}