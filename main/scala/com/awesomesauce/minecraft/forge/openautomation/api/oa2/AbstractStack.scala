package com.awesomesauce.minecraft.forge.openautomation.api.oa2

import li.cil.oc.api.machine.{Arguments, Context, Value}

abstract class AbstractStack extends Value with IOAStack {
  override def apply(context: Context, arguments: Arguments) = null

  override def unapply(context: Context, arguments: Arguments) = {}

  override def call(context: Context, arguments: Arguments) = throw new RuntimeException

  override def dispose(context: Context) = {}
}
