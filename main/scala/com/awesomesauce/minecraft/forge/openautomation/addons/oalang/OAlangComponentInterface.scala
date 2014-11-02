package com.awesomesauce.minecraft.forge.openautomation.addons.oalang

import li.cil.oc.api.machine.Machine

class ComponentInterface(val machine: Machine) {
  def invoke(address: String, callback: String, arguments: Array[AnyRef]): Array[AnyRef] = machine.invoke(address, callback, arguments)

  def list() = machine.components()
}
