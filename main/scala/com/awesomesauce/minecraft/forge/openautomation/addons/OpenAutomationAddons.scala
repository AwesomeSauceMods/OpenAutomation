package com.awesomesauce.minecraft.forge.openautomation.addons

import com.awesomesauce.minecraft.forge.core.lib.TAwesomeSauceMod
import com.awesomesauce.minecraft.forge.core.lib.util.ItemUtil
import com.awesomesauce.minecraft.forge.openautomation.addons.tile.TileEntityPressureCrusher
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.item.Item
import cpw.mods.fml.common.Mod.EventHandler
import com.awesomesauce.minecraft.forge.openautomation.common.OpenAutomation
import com.awesomesauce.minecraft.forge.core.components.AwesomeSauceComponents
import net.minecraft.item.ItemStack

@Mod(modid = OpenAutomationAddons.MODID, name = OpenAutomationAddons.MODNAME, modLanguage = "scala")
object OpenAutomationAddons extends TAwesomeSauceMod {

  @EventHandler
  def aspri(e: FMLPreInitializationEvent) = super.awesomesaucepreinit(e)
  @EventHandler
  def asi(e: FMLInitializationEvent) = super.awesomesauceinit(e)
  @EventHandler
  def aspoi(e: FMLPostInitializationEvent) = super.awesomesaucepostinit(e)
  var pressureCrusher: Block = null
  var reactorCore: Block = null
  var reactorInput: Block = null
  var reactorOutput: Block = null
  var reactorController: Block = null
  var reactorTurbine: Block = null
  var elements: scala.collection.mutable.Map[Int, Item] = scala.collection.mutable.Map[Int, Item]()
  final val MODID = "OpenAutomationAddons"
  final val MODNAME = "OpenAutomation Addons"
  def getModID: String = MODID
  def getModName: String = MODNAME
  def getTabIconItem: () => net.minecraft.item.Item = () => OpenAutomation.toolBase
  def getTextureDomain: String = "openautomationaddons"
  def preInit() = {}
  def init() = {
    pressureCrusher = ItemUtil.makeBlock(this, "pressureCrusher", Material.iron, () => new TileEntityPressureCrusher)

    for (i <- Range(1, 83)) {
      elements.put(i, ItemUtil.makeItem(this, "element" + i))
    }
  }
  def postInit() = {}
}