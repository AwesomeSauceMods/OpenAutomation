package com.awesomesauce.minecraft.forge.openautomation.common

import com.awesomesauce.minecraft.forge.core.lib.TAwesomeSauceMod
import com.awesomesauce.minecraft.forge.core.lib.item.ItemDescription
import com.awesomesauce.minecraft.forge.core.lib.util.ItemUtil
import com.awesomesauce.minecraft.forge.openautomation.common.item.ItemCodeBundle
import com.awesomesauce.minecraft.forge.openautomation.common.oc.OpenAutomationOC
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.{Loader, Mod, ModMetadata}
import net.minecraft.item.Item

@Mod(modid = OpenAutomation.MODID, name = OpenAutomation.MODNAME, modLanguage = "scala")
object OpenAutomation extends TAwesomeSauceMod with OAModule {
  final val MODID = "OpenAutomation"
  final val MODNAME = "OpenAutomation"
  val modules = scala.collection.mutable.Set[OAModule]()
  @Mod.Metadata(MODID)
  var metadata: ModMetadata = null
  var codeBundle: Item = null
  var inputCode: Item = null
  var itemCode: Item = null
  var fluidCode: Item = null
  var outputCode: Item = null
  var toolBase: Item = null

  @EventHandler
  def aspri(e: FMLPreInitializationEvent) = super.awesomesaucepreinit(e)

  @EventHandler
  def asi(e: FMLInitializationEvent) = super.awesomesauceinit(e)

  @EventHandler
  def aspoi(e: FMLPostInitializationEvent) = super.awesomesaucepostinit(e)

  def getModID: String = MODID

  def getModName: String = MODNAME

  override def getVersion = "0.3.0"

  def getTabIconItem: () => net.minecraft.item.Item = () => codeBundle

  def getTextureDomain: String = "openautomation"

  def preInit() = {
    if (Loader.isModLoaded("OpenComputers")) {
      modules.add(new OpenAutomationOC)
    }
    inputCode = ItemUtil.makeItem(oa, "oaInputCode", true)
      .addDescriptionLine("openautomation.code.desc")
      .addDescriptionLine("openautomation.inputCode.desc")
    itemCode = ItemUtil.makeItem(oa, "oaItemCode", true)
      .addDescriptionLine("openautomation.code.desc")
      .addDescriptionLine("openautomation.itemCode.desc")
    outputCode = ItemUtil.makeItem(oa, "oaOutputCode", true)
      .addDescriptionLine("openautomation.code.desc")
      .addDescriptionLine("openautomation.outputCode.desc")
    fluidCode = ItemUtil.makeItem(oa, "oaFluidCode", true)
      .addDescriptionLine("openautomation.code.desc")
      .addDescriptionLine("openautomation.fluidCode.desc")
    codeBundle = ItemUtil.makeItem(oa, "codeBundle", new ItemCodeBundle).asInstanceOf[ItemDescription]
      .addDescriptionLine("openautomation.codeBundle.desc")
      .addUsage("awesomesauce.rightclick", "openautomation.codeBundle.usage")
    toolBase = ItemUtil.makeItem(oa, "toolBase")
      .addDescriptionLine("openautomation.toolBase.desc").indev
    for (m <- modules) {
      m.preInit()
    }
  }

  def init() = {
    for (m <- modules) {
      m.init()
    }
  }

  def postInit() = {
    for (m <- modules) {
      m.postInit()
    }
  }
}