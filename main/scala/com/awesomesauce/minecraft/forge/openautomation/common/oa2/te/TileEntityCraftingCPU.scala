package com.awesomesauce.minecraft.forge.openautomation.common.oa2.te

import li.cil.oc.api.Network
import li.cil.oc.api.network.Visibility
import li.cil.oc.api.prefab.TileEntityEnvironment

class TileEntityCraftingCPU extends TileEntityEnvironment {
  val node_ = Network.newNode(this, Visibility.Network).withComponent("oaCPU").withConnector(100).create()
  node = node_

}
