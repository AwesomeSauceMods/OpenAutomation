package com.awesomesauce.minecraft.forge.openautomation.addons

import com.awesomesauce.minecraft.forge.core.lib.TAwesomeSauceMod
import com.awesomesauce.minecraft.forge.openautomation.common.OpenAutomation
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.{Mod, ModMetadata}

@Mod(modid = OpenAutomationAddons.MODID, name = OpenAutomationAddons.MODNAME, modLanguage = "scala")
object OpenAutomationAddons extends TAwesomeSauceMod {

  final val MODID = "OpenAutomationAddons"
  final val MODNAME = "OpenAutomation Addons"
  @Mod.Metadata(MODID)
  var metadata: ModMetadata = null

  @EventHandler
  def aspri(e: FMLPreInitializationEvent) = super.awesomesaucepreinit(e)

  @EventHandler
  def asi(e: FMLInitializationEvent) = super.awesomesauceinit(e)

  @EventHandler
  def aspoi(e: FMLPostInitializationEvent) = super.awesomesaucepostinit(e)

  def getModID: String = MODID

  def getModName: String = MODNAME

  def getTabIconItem: () => net.minecraft.item.Item = () => OpenAutomation.toolBase

  def getTextureDomain: String = "openautomationaddons"

  def preInit() = {}

  def init() = {
  }

  def postInit() = {}
}