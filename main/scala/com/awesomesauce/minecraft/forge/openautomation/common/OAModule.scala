package com.awesomesauce.minecraft.forge.openautomation.common

trait OAModule {
  val oa = OpenAutomation
  val name: String

  def preInit()

  def init()

  def postInit()
}
