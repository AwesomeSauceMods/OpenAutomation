package com.awesomesauce.minecraft.forge.openautomation.api.oa2

import li.cil.oc.api.network.Environment

trait IOAEnvironment extends Environment {
  def bindCore(core: IOACore)
}
