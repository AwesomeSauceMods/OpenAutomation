package com.awesomesauce.minecraft.forge.openautomation.common.te
import com.awesomesauce.minecraft.forge.openautomation.api.FluidDestination
import com.awesomesauce.minecraft.forge.openautomation.api.FluidStorage
import com.awesomesauce.minecraft.forge.openautomation.api.tools.AddressPastable
import com.awesomesauce.minecraft.forge.openautomation.api.tools.SideDefinable
import li.cil.oc.api.Network
import li.cil.oc.api.network.Arguments
import li.cil.oc.api.network.Callback
import li.cil.oc.api.network.Context
import li.cil.oc.api.network.Visibility
import li.cil.oc.api.prefab.TileEntityEnvironment
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.common.util.ForgeDirection
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidTankInfo
import net.minecraftforge.fluids.IFluidHandler
import scala.Array.canBuildFrom
import scala.collection.JavaConversions._

class TileEntityFluidIO extends TileEntityEnvironment with FluidStorage with SideDefinable with AddressPastable {
  val node_ = Network.newNode(this, Visibility.Network).withComponent("itemIO").withConnector(200).create()
  node = node_
  var side: ForgeDirection = ForgeDirection.UNKNOWN
  var drainSide: ForgeDirection = ForgeDirection.UNKNOWN
  def getDrainSide = if (drainSide == ForgeDirection.UNKNOWN) side.getOpposite() else drainSide
  var filter: String = "*"
  var address: String = "xxx"
  var drainAmount: Int = 1000
  //AddressPastable
  def pasteAddress(a: String) = address = a
  //SideDefinable
  def setSide(s: ForgeDirection) = side = s
  def fluidHandler: IFluidHandler = {
    val x = xCoord + side.offsetX
    val y = yCoord + side.offsetY
    val z = zCoord + side.offsetZ
    if (worldObj.getTileEntity(x, y, z).isInstanceOf[IFluidHandler])
      return worldObj.getTileEntity(x, y, z).asInstanceOf[IFluidHandler]
    else return null
  }
  //FluidStorage
  def recieveFluid(stack: FluidStack): Boolean = if (fluidHandler.fill(drainSide, stack, false) == stack.amount) {
    fluidHandler.fill(drainSide, stack, true)
    return true
  }
  else return false
  def getTankInfo(): Array[FluidTankInfo] = fluidHandler.getTankInfo(drainSide)
  def sendFluid(fluid: FluidStack): Unit = fluidHandler.drain(drainSide, fluid, true)
  //Callbacks
  @Callback
  def ping(context: Context, arguments: Arguments): Array[AnyRef] = Array(this.node.address(), "pong")
  @Callback
  def setSide(context: Context, arguments: Arguments): Array[AnyRef] = {
    side = ForgeDirection.valueOf(arguments.checkString(0).toUpperCase())
    Array(true.asInstanceOf[java.lang.Boolean])
  }
  @Callback
  def getSide(context: Context, arguments: Arguments): Array[AnyRef] = {
    Array(side.toString())
  }
  @Callback
  def setDrainSide(context: Context, arguments: Arguments): Array[AnyRef] = {
    drainSide = ForgeDirection.valueOf(arguments.checkString(0).toUpperCase())
    Array(true.asInstanceOf[java.lang.Boolean])
  }
  @Callback
  def getDrainSide(context: Context, arguments: Arguments): Array[AnyRef] = {
    Array(getDrainSide.toString())
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
  def doSendFluid(stack: FluidStack, destination: FluidDestination): Boolean = {
    if (destination.recieveFluid(stack)) {
      fluidHandler.drain(getDrainSide, stack, true)
      return true
    }
    return false
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
    if (!node.network().node(address).host().isInstanceOf[FluidDestination])
      return Array(null, "Not a fluid destination")
    val destination = node.network().node(address).host().asInstanceOf[FluidDestination]
    var stackToDrain: FluidStack = null
    if (filter == "*")
      stackToDrain = fluidHandler.drain(drainSide, drainAmount, false)
    else
      stackToDrain = fluidHandler.drain(drainSide, new FluidStack(FluidRegistry.getFluid(filter), drainAmount), false)
    if (doSendFluid(stackToDrain, destination)) {
      context.pause(0.5)
      node_.tryChangeBuffer(-10+drainAmount*0.05)
      address = oldAddress
      filter = oldFilter
      return Array(true.asInstanceOf[java.lang.Boolean])
    }
    address = oldAddress
    filter = oldFilter
    Array(true.asInstanceOf[java.lang.Boolean])
  }
  @Callback
  def getFluids(context: Context, arguments: Arguments): Array[AnyRef] = {
    val tankInfo = fluidHandler.getTankInfo(drainSide)
    return Array(tankInfo.map((a: FluidTankInfo) => Map("fluid" -> a.fluid.getFluid().getName(), "amount" -> a.fluid.amount.asInstanceOf[Integer])).asInstanceOf[java.util.Map[AnyRef, AnyRef]])
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
    tag.setString("side", side.toString())
    tag.setString("drainSide", drainSide.toString())
    tag.setInteger("drainAmount", drainAmount)
  }
}