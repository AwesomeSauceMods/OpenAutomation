package com.awesomesauce.minecraft.forge.openautomation.common

import com.awesomesauce.minecraft.forge.core.lib.TAwesomeSauceMod
import com.awesomesauce.minecraft.forge.core.lib.util.ItemUtil
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.OpenAutomationLasers
import com.awesomesauce.minecraft.forge.openautomation.common.oc.OpenAutomationOC
import com.awesomesauce.minecraft.forge.openautomation.common.tconstruct.OpenAutomationTConstruct
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.{Loader, Mod, ModMetadata}
import net.minecraft.item.{Item, ItemStack}
import net.minecraftforge.oredict.{OreDictionary, ShapedOreRecipe}

@Mod(modid = OpenAutomation.MODID, name = OpenAutomation.MODNAME, modLanguage = "scala", dependencies = "after:OpenComputers; after:TConstruct")
object OpenAutomation extends TAwesomeSauceMod with OAModule {
  final val MODID = "OpenAutomation"
  final val MODNAME = "OpenAutomation"
  val name = "Core"
  val modules = scala.collection.mutable.Set[OAModule]()
  @Mod.Metadata(MODID)
  var metadata: ModMetadata = null

  var laserFocus: Item = null
  var laserEmitter: Item = null
  var laserReceptor: Item = null
  var laserMirrorCrafting: Item = null

  @EventHandler
  def aspri(e: FMLPreInitializationEvent) = super.awesomesaucepreinit(e)

  @EventHandler
  def asi(e: FMLInitializationEvent) = super.awesomesauceinit(e)

  @EventHandler
  def aspoi(e: FMLPostInitializationEvent) = super.awesomesaucepostinit(e)

  def getModID: String = MODID

  def getModName: String = MODNAME

  override def getVersion = "0.3.0"

  def getTabIconItem: () => net.minecraft.item.Item = () => laserFocus

  def getTextureDomain: String = "openautomation"

  def preInit() = {
    addModules()
    for (m <- modules) {
      m.preInit()
    }
    laserFocus = ItemUtil.makeItem(oa, "laserFocus", true)
    laserEmitter = ItemUtil.makeItem(oa, "laserEmitter", true)
    laserReceptor = ItemUtil.makeItem(oa, "laserReceptor", true)
    laserMirrorCrafting = ItemUtil.makeItem(oa, "laserMirrorCrafting")

    OreDictionary.registerOre("laserMirror", laserMirrorCrafting)
    ItemUtil.addRecipe(oa, new ShapedOreRecipe(new ItemStack(laserFocus, 2), "aba", " a ",
      Character.valueOf('b'), "blockGlass", Character.valueOf('a'), "nuggetAwesomeite"))
    ItemUtil.addRecipe(oa, new ShapedOreRecipe(new ItemStack(laserEmitter), "aaa", " ba", "  a",
      Character.valueOf('b'), "nuggetAwesomeite", Character.valueOf('a'), "dustGlowstone"))
    ItemUtil.addRecipe(oa, new ShapedOreRecipe(new ItemStack(laserReceptor), "aaa", " aa", "  a",
      Character.valueOf('a'), "nuggetAwesomeite"))
    ItemUtil.addRecipe(oa, new ShapedOreRecipe(new ItemStack(laserMirrorCrafting, 2), "aba", "aba", " c ",
      Character.valueOf('a'), "dustGlowstone", Character.valueOf('b'), "paneGlass", Character.valueOf('c'), "nuggetAwesomeite"))
  }

  def addModules() = {
    if (Loader.isModLoaded("OpenComputers")) {
      addModule(OpenAutomationOC)
    }
    if (Loader.isModLoaded("TConstruct")) {
      addModule(OpenAutomationTConstruct)
    }
    addModule(OpenAutomationLasers)
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