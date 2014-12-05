package com.awesomesauce.minecraft.forge.openautomation.common.oc

import com.awesomesauce.minecraft.forge.core.lib.item.BlockSimpleContainer
import li.cil.oc.api.driver._
import li.cil.oc.api.network.Environment
import net.minecraft.item.{ItemBlock, ItemStack}
import net.minecraft.world.World

object DummyBlockDriver extends Block with EnvironmentAware {
  override def worksWith(world: World, x: Int, y: Int, z: Int) = false

  override def createEnvironment(world: World, x: Int, y: Int, z: Int) = null

  override def providedEnvironment(stack: ItemStack): Class[_ <: Environment] = {
    if (stack.getItem.isInstanceOf[ItemBlock]) {
      val block = stack.getItem.asInstanceOf[ItemBlock].field_150939_a
      if (block.isInstanceOf[BlockSimpleContainer]) {
        val tile = block.asInstanceOf[BlockSimpleContainer].tile()
        if (tile.isInstanceOf[Environment]) {
          return tile.asInstanceOf[Environment].getClass
        }
      }
    }
    null
  }
}
