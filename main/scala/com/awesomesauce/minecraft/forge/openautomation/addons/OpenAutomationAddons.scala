package com.awesomesauce.minecraft.forge.openautomation.addons

import com.awesomesauce.minecraft.forge.core.lib.TAwesomeSauceMod
import com.awesomesauce.minecraft.forge.core.lib.util.ItemUtil
import com.awesomesauce.minecraft.forge.openautomation.addons.tile._
import com.awesomesauce.minecraft.forge.openautomation.common.OpenAutomation
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.{Mod, ModMetadata}
import li.cil.oc.api.{FileSystem, Items}
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.oredict.ShapedOreRecipe

@Mod(modid = OpenAutomationAddons.MODID, name = OpenAutomationAddons.MODNAME, modLanguage = "scala")
object OpenAutomationAddons extends TAwesomeSauceMod {

  final val MODID = "OpenAutomationAddons"
  final val MODNAME = "OpenAutomation Addons"
  var pressureCrusher: Block = null
  var powerOutput: Block = null
  var reactorCore: Block = null
  var reactorInput: Block = null
  var reactorOutput: Block = null
  var reactorController: Block = null
  var reactorTurbine: Block = null
  var elements: scala.collection.mutable.Map[Int, Item] = scala.collection.mutable.Map[Int, Item]()
  var autoOSFS: li.cil.oc.api.fs.FileSystem = null
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
    pressureCrusher = ItemUtil.makeBlock(this, "pressureCrusher", Material.iron, () => new TileEntityPressureCrusher)
    powerOutput = ItemUtil.makeBlock(this, "powerOutput", Material.iron, () => new TileEntityPowerOutput)
    if (this.config.getBoolean("enableChemistry", Configuration.CATEGORY_GENERAL, false, "Enable the chemistry component. NON COMPLETE")) {
      for (i <- Range(1, 83)) {
        elements.put(i, ItemUtil.makeItem(this, "element" + i))
      }
    }
    autoOSFS = FileSystem.fromClass(Class.forName("com.awesomesauce.minecraft.forge.openautomation.addons.OpenAutomationAddons"), "openautomationaddons", "lua/autoos")
    ItemUtil.addRecipe(this, new ShapedOreRecipe(new ItemStack(powerOutput),
      "xyx",
      "jad",
      "xmx",
      Character.valueOf('x'), "ingotIron", Character.valueOf('y'), "ingotGold",
      Character.valueOf('j'), Items.get("cable").createItemStack(1), Character.valueOf('a'), "ingotAwesomeite",
      Character.valueOf('d'), Items.get("printedCircuitBoard").createItemStack(1)), Character.valueOf('m'), "oaOutputCode")
  }

  def postInit() = {}
}