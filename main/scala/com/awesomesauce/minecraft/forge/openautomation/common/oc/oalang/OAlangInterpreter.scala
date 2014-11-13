package com.awesomesauce.minecraft.forge.openautomation.common.oc.oalang

class OAlangInterpreter(val components: ComponentInterface) {
  components.interpreter = this
  val script = scala.collection.mutable.ArrayBuffer[ScriptPart]()
  val variableMap = scala.collection.mutable.HashMap[String, AnyRef]()
  var index = 0

  def invoke(component: String, callback: String, arguments: Array[AnyRef]): Array[AnyRef] = {
    if (component == "interpreter") {
      if (callback == "setIndex") {
        index = arguments(0).asInstanceOf[Integer].toInt
        return Array[AnyRef](java.lang.Boolean.TRUE)
      }
      else if (callback == "getIndex") {
        return Array[AnyRef](index.asInstanceOf[Integer])
      }
    }
    components.invoke(component, callback, arguments)
  }

  def runCall(): Boolean = {
    try {
      script(index).execute(this)
      index += 1
      true
    }
    catch {
      case e: ArrayIndexOutOfBoundsException => false
    }
  }

  def handleArgument(a: String): AnyRef = {
    if (a.charAt(0) == '@') {
      return variableMap(a.substring(1))
    }
    try {
      return a.toDouble.asInstanceOf[java.lang.Double]
    }
    catch {
      case e: NumberFormatException => None
    }
    try {
      return a.toInt.asInstanceOf[Integer]
    }
    catch {
      case e: NumberFormatException => None
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
      if (c.startsWith("c.")) {
        if (comp.startsWith(c.substring(2))) {
          return comp
        }
      }
    }
    c
  }
}
