package com.awesomesauce.minecraft.forge.openautomation.common

trait OAModule {
  val oa = OpenAutomation

  def preInit()

  def init()

  def postInit()
}
