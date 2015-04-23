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

  def preInit() = {
    baubleRingBase = ItemUtil.makeItem(oa, "baubleRingBase")
    baubleAmuletBase = ItemUtil.makeItem(oa, "baubleAmuletBase")
    baubleBeltBase = ItemUtil.makeItem(oa, "baubleBeltBase")
    baubleRing = ItemUtil.makeItem(oa, "baubleRing", new ItemMachineBauble(BaubleType.RING))
    baubleAmulet = ItemUtil.makeItem(oa, "baubleAmulet", new ItemMachineBauble(BaubleType.AMULET))
    baubleBelt = ItemUtil.makeItem(oa, "baubleBelt", new ItemMachineBauble(BaubleType.BELT))
  }

  def init() = {

  }

  def postInit() = {

  }
}
