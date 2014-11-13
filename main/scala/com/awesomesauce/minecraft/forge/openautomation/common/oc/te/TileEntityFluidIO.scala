package com.awesomesauce.minecraft.forge.openautomation.common.oc.te

import com.awesomesauce.minecraft.forge.core.lib.item.TCustomTexture
import com.awesomesauce.minecraft.forge.openautomation.api.oc.tools.AddressPastable
import com.awesomesauce.minecraft.forge.openautomation.api.tools.SideDefinable
import com.awesomesauce.minecraft.forge.openautomation.api.{FluidDestination, FluidStorage}
import com.awesomesauce.minecraft.forge.openautomation.common.Util
import li.cil.oc.api.Network
import li.cil.oc.api.machine.{Arguments, Callback, Context}
import li.cil.oc.api.network.Visibility
import li.cil.oc.api.prefab.TileEntityEnvironment
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.{FluidRegistry, FluidStack, FluidTankInfo, IFluidHandler}

import scala.Array.canBuildFrom

class TileEntityFluidIO extends TileEntityEnvironment with FluidStorage with SideDefinable with AddressPastable with TCustomTexture {
  try {
    val node_ = Network.newNode(this, Visibility.Network).withComponent("fluidIO").withConnector(200).create()
    node = node_
  }
  catch {
    case e: NullPointerException => node = null
  }
  var side: ForgeDirection = ForgeDirection.UNKNOWN
  var drainSide: ForgeDirection = ForgeDirection.UNKNOWN
  var filter = ""
  var address: String = "xxx"
  var drainAmount: Int = 1000

  //AddressPastable
  def pasteAddress(a: String) = address = a

  //SideDefinable
  def setSide(s: ForgeDirection) = side = s

  def getTextureForSide(side: Int): Int = if (ForgeDirection.getOrientation(side) == this.side) 1 else if (ForgeDirection.getOrientation(side) == drainSide) return 2 else return 0

  //FluidStorage
  def recieveFluid(stack: FluidStack): Boolean = if (fluidHandler.fill(drainSide, stack, false) == stack.amount) {
    fluidHandler.fill(drainSide, stack, true)
    true
  }
  else false

  def getTankInfo(): Array[FluidTankInfo] = fluidHandler.getTankInfo(drainSide)

  def sendFluid(fluid: FluidStack): Unit = fluidHandler.drain(drainSide, fluid, true)

  //Callbacks
  @Callback
  def ping(context: Context, arguments: Arguments): Array[AnyRef] = Array(this.node.address(), "pong")

  @Callback
  def setSide(context: Context, arguments: Arguments): Array[AnyRef] = {
    side = ForgeDirection.valueOf(arguments.checkString(0).toUpperCase)
    Array(true.asInstanceOf[java.lang.Boolean])
  }

  @Callback
  def getSide(context: Context, arguments: Arguments): Array[AnyRef] = {
    Array(side.toString)
  }

  @Callback
  def setDrainSide(context: Context, arguments: Arguments): Array[AnyRef] = {
    drainSide = ForgeDirection.valueOf(arguments.checkString(0).toUpperCase)
    Array(true.asInstanceOf[java.lang.Boolean])
  }

  @Callback
  def getDrainSide(context: Context, arguments: Arguments): Array[AnyRef] = {
    Array(getDrainSide.toString)
  }

  @Callback
  def setFilter(context: Context, arguments: Arguments): Array[AnyRef] = {
    filter = arguments.checkString(0)
    Array(true.asInstanceOf[java.lang.Boolean])
  }

  @Callback
  def getFilter(context: Context, arguments: Arguments): Array[AnyRef] = {
    Array(filter)
  }

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
  def sendFluid(context: Context, arguments: Arguments): Array[AnyRef] = {
    context.pause(0.5)
    val oldAddress = address
    val oldFilter = filter
    if (arguments.isString(0))
      address = arguments.checkString(0)
    if (arguments.isString(0) && arguments.isString(1))
      filter = arguments.checkString(1)
    if (!arguments.isString(0) && arguments.isString(1))
      address = arguments.checkString(1)
    if (arguments.isString(2))
      filter = arguments.checkString(2)
    if (arguments.isInteger(0))
      drainAmount = arguments.checkInteger(0)
    if (arguments.isInteger(1))
      drainAmount = arguments.checkInteger(1)
    if (arguments.isInteger(2))
      drainAmount = arguments.checkInteger(2)
    if (!Util.isFluidDestination(node, address))
      return Array(null, "Not a fluid destination")
    val destination = Util.getFluidDestination(node, address)
    var stackToDrain: FluidStack = null
    if (filter == "")
      stackToDrain = fluidHandler.drain(drainSide, drainAmount, false)
    else
      stackToDrain = fluidHandler.drain(drainSide, new FluidStack(FluidRegistry.getFluid(filter), drainAmount), false)
    if (doSendFluid(stackToDrain, destination)) {
      context.pause(0.5)
      node_.tryChangeBuffer(-10 + drainAmount * 0.05)
      address = oldAddress
      filter = oldFilter
      return Array(true.asInstanceOf[java.lang.Boolean])
    }
    address = oldAddress
    filter = oldFilter
    Array(true.asInstanceOf[java.lang.Boolean])

  }

  def doSendFluid(stack: FluidStack, destination: FluidDestination): Boolean = {
    if (destination.recieveFluid(stack)) {
      fluidHandler.drain(getDrainSide, stack, true)
      return true
    }
    false
  }

  def fluidHandler: IFluidHandler = {
    val x = xCoord + side.offsetX
    val y = yCoord + side.offsetY
    val z = zCoord + side.offsetZ
    if (worldObj.getTileEntity(x, y, z).isInstanceOf[IFluidHandler])
      worldObj.getTileEntity(x, y, z).asInstanceOf[IFluidHandler]
    else null
  }

  def getDrainSide = if (drainSide == ForgeDirection.UNKNOWN) side.getOpposite else drainSide

  @Callback
  def getFluids(context: Context, arguments: Arguments): Array[AnyRef] = {
    val tankInfo = fluidHandler.getTankInfo(drainSide)
    if (tankInfo == null)
      return Array(null, "No tank?")
    Array(tankInfo.map((a: FluidTankInfo) => Map("fluid" -> a.fluid.getFluid.getName, "amount" -> a.fluid.amount.asInstanceOf[Integer])).asInstanceOf[java.util.Map[AnyRef, AnyRef]])
  }

  //Save/Load
  override def readFromNBT(tag: NBTTagCompound) = {
    super.readFromNBT(tag)
    filter = tag.getString("filter")
    address = tag.getString("address")
    side = ForgeDirection.valueOf(tag.getString("side"))
    drainSide = ForgeDirection.valueOf(tag.getString("drainSide"))
    drainAmount = tag.getInteger("drainAmount")
  }

  override def writeToNBT(tag: NBTTagCompound) = {
    super.writeToNBT(tag)
    tag.setString("filter", filter)
    tag.setString("address", address)
    tag.setString("side", side.toString)
    tag.setString("drainSide", drainSide.toString)
    tag.setInteger("drainAmount", drainAmount)
  }
}