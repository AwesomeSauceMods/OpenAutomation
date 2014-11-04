package com.awesomesauce.minecraft.forge.openautomation.addons.oalang

import li.cil.oc.api.machine.{Architecture, ExecutionResult, Machine}
import net.minecraft.nbt.{NBTTagCompound, NBTTagList, NBTTagString}
import net.minecraftforge.common.util.Constants.NBT

@Architecture.Name("OAlang")
class OAlangArchitecture(val machine: Machine) extends Architecture {
  var initialized = false
  var interpreter: OAlangInterpreter = null

  def isInitialized = initialized

  def recomputeMemory() = {
    //TODO
  }

  def initialize() = {
    interpreter = new OAlangInterpreter(new ComponentInterface(machine))
    true
  }

  def close() = {
    interpreter = null
  }

  def runSynchronized() = {
    interpreter.runCall()
  }

  def runThreaded(isSynchronizedReturn: Boolean): ExecutionResult = {
    new ExecutionResult.SynchronizedCall()
  }

  def onConnect() = {
    if (interpreter.script.length == 0) {
      interpreter.invoke("interpreter", "load", Array[AnyRef]("init.oa"))
    }
  }

  def load(nbt: NBTTagCompound) = {
    if (nbt.hasKey("interpreter")) {
      val tag = nbt.getCompoundTag("interpreter")
      val variables = tag.getCompoundTag("variables")
      val it = variables.func_150296_c().iterator()
      while (it.hasNext) {
        val vari = it.next.toString
        interpreter.variableMap(vari) = interpreter.handleArgument(variables.getString(vari))
      }
      val script = tag.getTagList("script", NBT.TAG_STRING)
      for (num <- Range(0, script.tagCount())) {
        interpreter.script.append(new ScriptPart(script.getStringTagAt(num)))
      }
      interpreter.index = tag.getInteger("index")
    }
  }

  def save(nbt: NBTTagCompound) = {
    val tag = new NBTTagCompound
    val variables = new NBTTagCompound
    for (variable <- interpreter.variableMap) {
      variables.setString(variable._1, variable._2.toString)
    }
    tag.setTag("variables", variables)
    val script = new NBTTagList
    for (line <- interpreter.script) {
      script.appendTag(new NBTTagString(line.toString))
    }
    tag.setTag("script", script)
    tag.setInteger("index", interpreter.index)
    nbt.setTag("interpreter", tag)
  }
}
