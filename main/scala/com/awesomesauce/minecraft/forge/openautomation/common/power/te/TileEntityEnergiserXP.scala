package com.awesomesauce.minecraft.forge.openautomation.common.power.te

import com.awesomesauce.minecraft.forge.openautomation.api.lasers.{LaserHelper, LaserPacket, LaserReciever}
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.packets.{EnergyPacket, EntityPacket}
import com.awesomesauce.minecraft.forge.openautomation.common.power.OpenAutomationPower
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraftforge.common.util.ForgeDirection

/**
 * Created by Sandra on 22/05/2015.
 */
class TileEntityEnergiserXP extends TileEntity with LaserReciever {
  var buffer: Int = 0

  def arrive(from: ForgeDirection, laser: LaserPacket) = {
    laser match {
      case e: EntityPacket =>
        e.entity match {
          case p: EntityPlayer => {
            val amount = Math.max(p.experience + 0.03F, OpenAutomationPower.xpMaxUse)
            p.addExperience(-Math.round(amount))
            buffer += amount * OpenAutomationPower.xpModifier
          }
        }
      case e: EnergyPacket =>
        if (laser.getCompound.hasKey("supportsEnergiser") && laser.getCompound.getBoolean("supportsEnergiser")) {
          e.amount += buffer
          buffer = 0
        }
    }
    LaserHelper.sendLaser(worldObj, xCoord, yCoord, zCoord, from.getOpposite, laser)
  }
}
