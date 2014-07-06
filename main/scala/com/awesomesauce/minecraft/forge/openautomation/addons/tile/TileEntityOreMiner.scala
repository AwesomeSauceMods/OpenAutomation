package com.awesomesauce.minecraft.forge.openautomation.addons.tile

import com.awesomesauce.minecraft.forge.openautomation.api.ItemInput
import com.awesomesauce.minecraft.forge.openautomation.api.tools.AddressPastable


class TileEntityOreMiner extends TileEntityEnvironment with AddressPastable with ItemInput {
  val node_ = Network.newNode(this, Visibility.Network).withComponent("pressurecrusher").withConnector(1000).create()
  node = node_
  var address: String = "xxx"
  var maxX: Int = 100
  var maxY: Int = 100
  var maxZ: Int = 100
  var minX: Int = 100
  var minY: Int = 100
  var minZ: Int = 100

  def pasteAddress(add: String) = address = add

  @Callback
  def setAddress(context: Context, arguments: Arguments): Array[AnyRef] = {
    address = arguments.checkString(0)
    Array(true.asInstanceOf[java.lang.Boolean])
  }

  @Callback
  def getAddress(context: Context, arguments: Arguments): Array[AnyRef] = {
    Array(address)
  }

  @Callback
  def setMin(context: Context, arguments: Arguments): Array[AnyRef] = {
    minX = arguments.checkInteger(0)
    minY = arguments.checkInteger(1)
    minZ = arguments.checkInteger(2)
    Array(true.asInstanceOf[java.lang.Boolean])
  }

  @Callback
  def getMin(context: Context, arguments: Arguments): Array[AnyRef] = {
    Array(minX, minY, minZ)
  }

  @Callback
  def setMax(context: Context, arguments: Arguments): Array[AnyRef] = {
    maxX = arguments.checkInteger(0)
    maxY = arguments.checkInteger(1)
    maxZ = arguments.checkInteger(2)
    Array(true.asInstanceOf[java.lang.Boolean])
  }

  @Callback
  def getMax(context: Context, arguments: Arguments): Array[AnyRef] = {
    Array(maxX, maxY, maxZ)
  }

  @Callback
  def mine(context: Context, arguments: Arguments): Array[AnyRef] = {
    //TODO
  }
}
