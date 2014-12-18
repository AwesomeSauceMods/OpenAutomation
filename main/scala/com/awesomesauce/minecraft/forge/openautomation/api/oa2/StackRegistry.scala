package com.awesomesauce.minecraft.forge.openautomation.api.oa2

object StackRegistry {
  val registry = scala.collection.mutable.Map[String, () => IOAStack]()

  def registerStackType(stype: String, creator: () => IOAStack) = {
    registry(stype) = creator
  }
}
