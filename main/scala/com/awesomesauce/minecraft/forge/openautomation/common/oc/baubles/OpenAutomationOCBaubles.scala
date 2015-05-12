package com.awesomesauce.minecraft.forge.openautomation.common.oc.baubles

import baubles.api.BaubleType
import com.awesomesauce.minecraft.forge.core.lib.util.ItemUtil
import com.awesomesauce.minecraft.forge.openautomation.common.OAModule
import com.awesomesauce.minecraft.forge.openautomation.common.oc.baubles.item.ItemMachineBauble
import net.minecraft.item.Item


object OpenAutomationOCBaubles extends OAModule {
  val name = "OC.Baubles"

  var baubleRingBase: Item = null
  var baubleAmuletBase: Item = null
  var baubleBeltBase: Item = null
  var baubleRing: Item = null
  var baubleAmulet: Item = null
  var baubleBelt: Item = null

  var baubleModem: Item = null
  def preInit() = {
  }

  def init() = {
    baubleRingBase = ItemUtil.makeItem(oa, "baubleRingBase").indev.asInstanceOf[Item]
    baubleAmuletBase = ItemUtil.makeItem(oa, "baubleAmuletBase").indev.asInstanceOf[Item]
    baubleBeltBase = ItemUtil.makeItem(oa, "baubleBeltBase").indev.asInstanceOf[Item]
    baubleRing = ItemUtil.makeItem(oa, "baubleRing", new ItemMachineBauble(BaubleType.RING))
    baubleAmulet = ItemUtil.makeItem(oa, "baubleAmulet", new ItemMachineBauble(BaubleType.AMULET))
    baubleBelt = ItemUtil.makeItem(oa, "baubleBelt", new ItemMachineBauble(BaubleType.BELT))
    //baubleModem = ItemUtil.makeItem(oa, "baubleModem")
    BaubleAssembler.register()
  }

  def postInit() = {

  }
}
