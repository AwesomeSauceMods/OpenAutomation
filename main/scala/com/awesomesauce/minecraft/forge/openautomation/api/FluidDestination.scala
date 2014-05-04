package com.awesomesauce.minecraft.forge.openautomation.api

import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidTankInfo

trait FluidDestination {
	def recieveFluid(stack: FluidStack): Boolean
	def getTankInfo() : Array[FluidTankInfo]
}