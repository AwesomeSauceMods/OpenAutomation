package com.awesomesauce.minecraft.forge.openautomation.common

import com.awesomesauce.minecraft.forge.core.lib.TAwesomeSauceMod
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.OpenAutomationLasers
import com.awesomesauce.minecraft.forge.openautomation.common.oc.OpenAutomationOC
import com.awesomesauce.minecraft.forge.openautomation.common.tconstruct.OpenAutomationTConstruct
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.{Loader, Mod, ModMetadata}
import net.minecraft.item.Item

@Mod(modid = OpenAutomation.MODID, name = OpenAutomation.MODNAME, modLanguage = "scala")
object OpenAutomation extends TAwesomeSauceMod with OAModule {
  final val MODID = "OpenAutomation"
  final val MODNAME = "OpenAutomation"
  val name = "Core"
  val modules = scala.collection.mutable.Set[OAModule]()
  @Mod.Metadata(MODID)
  var metadata: ModMetadata = null
  var codeBundle: Item = null
  var inputCode: Item = null
  var itemCode: Item = null
  var fluidCode: Item = null
  var outputCode: Item = null
  var toolBase: Item = null
  var toolHeadSideDefiner: Item = null
  var toolSideDefiner: Item = null

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
      addModule(OpenAutomationOC)
    }
    if (Loader.isModLoaded("TConstruct")) {
      addModule(OpenAutomationTConstruct)
    }
    addModule(OpenAutomationLasers)
    for (m <- modules) {
      m.preInit()
    }
  }

  def addModule(module: OAModule) = {
    if (config.get("Modules", module.name, true).getBoolean) {
      modules.add(module)
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