package com.awesomesauce.minecraft.forge.openautomation.addons.oalang


class ScriptPart(line: String) {
  val splittedLine = line.split(" > ")
  val commandSplit: Array[String] = splittedLine(0).split(" ")
  if (splittedLine.length > 1) {
    resultVars = splittedLine(1).split(" ")
  }
  val component = commandSplit(0)
  val callback = commandSplit(1)
  val arguments = commandSplit.drop(2)
  var resultVars: Array[String] = null

  def execute(int: OAlangInterpreter) = {
    val results = int.components.invoke(int.handleComponent(component), int.handleCallback(callback), arguments.map((a: String) => int.handleArgument(a)))
    for (result <- Range(0, results.length)) {
      int.variableMap(resultVars(result)) = results(result)
    }
  }
}
