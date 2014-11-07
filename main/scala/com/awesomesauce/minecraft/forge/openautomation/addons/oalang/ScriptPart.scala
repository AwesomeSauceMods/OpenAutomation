package com.awesomesauce.minecraft.forge.openautomation.addons.oalang


class ScriptPart(line: String) {
  val splittedLine = line.split(" > ")
  val commandSplit: Array[String] = splittedLine(0).split(" ")
  val component = commandSplit(0)
  if (splittedLine.length > 1) {
    resultVars = splittedLine(1).split(" ")
  }
  val callback = commandSplit(1)
  val arguments = commandSplit.drop(2)
  var resultVars = Array[String]("")

  override def toString = line
  def execute(int: OAlangInterpreter) = {
    val c = int.handleArgument(component).toString
    val ca = int.handleArgument(callback).toString
    val a = arguments.map((a: String) => int.handleArgument(a))
    val results = int.invoke(c, ca, a)
    println(c + "." + ca + a)
    for (result <- Range(0, results.length)) {
      int.variableMap.put(resultVars(result), results(result))
    }
  }
}
