package com.awesomesauce.minecraft.forge.openautomation.addons.oalang

import li.cil.oc.api.machine.{Architecture, ExecutionResult, Machine}
import net.minecraft.nbt.NBTTagCompound

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
    //TODO
  }

  def load(nbt: NBTTagCompound) = {
    //TODO
  }

  def save(nbt: NBTTagCompound) = {
    //TODO
  }
}
