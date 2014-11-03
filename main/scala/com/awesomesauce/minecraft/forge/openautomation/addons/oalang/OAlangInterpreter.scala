package com.awesomesauce.minecraft.forge.openautomation.addons.oalang

class OAlangInterpreter(val components: ComponentInterface) {
  components.interpreter = this
  val script = scala.collection.mutable.ArrayBuffer[ScriptPart]()
  val variableMap = scala.collection.mutable.HashMap[String, AnyRef]()
  var index = 0

  def invoke(component: String, callback: String, arguments: Array[AnyRef]): Array[AnyRef] = {
    components.invoke(component, callback, arguments)
  }
  def runCall() = {
    script(index).execute(this)
    index += 1
  }

  def handleArgument(a: String): AnyRef = {
    if (a.charAt(0) == '@') {
      return variableMap(a.substring(1))
    }
    handleComponent(a)
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
}
