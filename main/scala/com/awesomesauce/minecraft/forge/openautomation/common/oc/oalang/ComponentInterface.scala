package com.awesomesauce.minecraft.forge.openautomation.common.oc.oalang

import li.cil.oc.api.machine.Machine

class ComponentInterface(val machine: Machine) {
  var interpreter: OAlangInterpreter = null

  def invoke(address: String, callback: String, arguments: Array[AnyRef]): Array[AnyRef] = {
    if (address == "interpreter") {
      if (callback == "load") {
        interpreter.index = 0
        try {
          val script = arguments(0)
          val bootAddress = machine.getBootAddress
          var string = ""
          var fullString = ""
          val handle = machine.invoke(bootAddress, "open", Array[AnyRef](script))(0)
          while (string != null) {
            fullString = fullString + string
            string = machine.invoke(bootAddress, "read", Array[AnyRef](handle, 10.asInstanceOf[Integer]))(0).asInstanceOf[String]
          }
          val lines = fullString.split("\n")
          interpreter.script.clear()
          for (line <- lines) {
            interpreter.script.append(new ScriptPart(line))
          }
          return Array[AnyRef](java.lang.Boolean.TRUE)
        }
        catch {
          case e: Exception => return Array[AnyRef](null, e.toString)
        }
      }
      if (callback == "setBootAddress") {
        if (list().get(arguments(0).asInstanceOf[String]) == "filesystem") {
          machine.setBootAddress(arguments(0).asInstanceOf[String])
          return Array[AnyRef](java.lang.Boolean.TRUE)
        }
        return Array[AnyRef](java.lang.Boolean.FALSE)
      }
    }
    machine.invoke(address, callback, arguments)
  }

  def list() = machine.components()
}
