package com.awesomesauce.minecraft.forge.openautomation.common.tconstruct

import com.awesomesauce.minecraft.forge.core.lib.util.ItemUtil
import com.awesomesauce.minecraft.forge.openautomation.common.OAModule
import com.awesomesauce.minecraft.forge.openautomation.common.tconstruct.te.TileEntityMelter
import net.minecraft.block.Block
import net.minecraft.block.material.Material

object OpenAutomationTConstruct extends OAModule {
  var melter: Block = null
  var compactSmeltery2: Block = null

  def preInit() = {

  }

  def init() = {
    melter = ItemUtil.makeBlock(oa, "tconstruct.melter", Material.rock, () => new TileEntityMelter)
  }

  def postInit() = {

  }
}
