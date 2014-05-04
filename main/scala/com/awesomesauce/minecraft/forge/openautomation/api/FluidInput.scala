package com.awesomesauce.minecraft.forge.openautomation.api

import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidTankInfo

trait FluidInput {
	def sendFluid(fluid: FluidStack)
	def getTankInfo() : Array[FluidTankInfo]
}