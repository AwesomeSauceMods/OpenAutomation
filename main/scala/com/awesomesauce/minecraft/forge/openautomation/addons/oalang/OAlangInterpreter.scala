package com.awesomesauce.minecraft.forge.openautomation.addons.oalang

class OAlangInterpreter(val components: ComponentInterface) {
  val script = scala.collection.mutable.ArraySeq[ScriptPart]()
  val variableMap = scala.collection.mutable.HashMap[String, AnyRef]()
  var index = 0

  def runCall() = {
    script(index).execute(this)
  }

  def handleArgument(a: String): AnyRef = {
    if (a.charAt(0) == '@') {
      return variableMap(a.substring(1))
    }
    a
  }

  def handleComponent(c: String): String = {
    val it = components.list().keySet().iterator()
    while (it.hasNext) {
      val comp = it.next()
      if (c.startsWith("p.")) {
        if (components.list().get(comp) == c.substring(2)) {
          return comp
        }
      }
      if (comp.startsWith(c)) {
        return comp
      }
    }
    c
  }

  def handleCallback(c: String): String = {
    c
  }
}
