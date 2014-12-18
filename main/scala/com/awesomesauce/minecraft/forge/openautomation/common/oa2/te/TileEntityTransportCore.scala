package com.awesomesauce.minecraft.forge.openautomation.common.oa2.te

import com.awesomesauce.minecraft.forge.openautomation.api.oa2.{IOACore, IOAEnvironment}
import li.cil.oc.api.Network
import li.cil.oc.api.machine.{Arguments, Callback, Context}
import li.cil.oc.api.network.Visibility
import li.cil.oc.api.prefab.TileEntityEnvironment

import scala.collection.mutable.ArrayBuffer

class TileEntityTransportCore extends TileEntityEnvironment with IOACore {
  val node_ = Network.newNode(this, Visibility.Network).withComponent("oaCore").withConnector(100).create()
  node = node_
  val componentList = ArrayBuffer[String]()

  @Callback
  def bind(context: Context, arguments: Arguments): Array[AnyRef] = {
    val addr = arguments.checkString(0)
    if (node_.network().node(addr).host().isInstanceOf[IOAEnvironment]) {
      node_.network().node(addr).host().asInstanceOf[IOAEnvironment].bindCore(this)
      componentList += addr
      Array(java.lang.Boolean.TRUE)
    }
    else {
      Array(java.lang.Boolean.FALSE)
    }
  }

  @Callback
  def createStack(context: Context, arguments: Arguments): Array[AnyRef] = {
    Array(null)
  }
}
