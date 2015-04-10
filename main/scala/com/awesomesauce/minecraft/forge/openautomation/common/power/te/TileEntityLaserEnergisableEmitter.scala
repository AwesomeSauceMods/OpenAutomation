package com.awesomesauce.minecraft.forge.openautomation.common.power.te

import com.awesomesauce.minecraft.forge.openautomation.common.lasers.packets.EnergyPacket
import com.awesomesauce.minecraft.forge.openautomation.common.lasers.te.TileEntityEnergyLaserEmitter

/**
 * Created by gjgfuj on 10/04/15.
 */
class TileEntityLaserEnergisableEmitter extends TileEntityEnergyLaserEmitter {
  var packetsSent = 0

  override def makePacket(energy: Int): EnergyPacket = {
    val packet = new EnergyPacket(energy)
    packetsSent += 1
    if (packetsSent % 100 == 0) {
      packet.getCompound.setBoolean("supportsEnergiser", true)
    }
    return packet
  }
}
