package com.awesomesauce.minecraft.forge.openautomation.api

import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary

class Filter(val filterString: String) {
  setFilterString(filterString)
  var filterText: String = ""
  var oreDict: Boolean = false

  def setFilterString(str: String) = {
    if (str.charAt(0) == "@") {
      oreDict = true
      filterText = str.substring(1)
    }
    else {
      filterText = str
    }
  }

  def doesFilterMatch(stack: ItemStack): Boolean = {
    if (oreDict) {
      val oreNames = OreDictionary.getOreNames(stack)
      for (ore <- oreNames) {
        if (ore.contains(filterText)) {
          return true
        }
      }
    }
    else {
      val name = stack.getUnlocalizedName
      if (name.contains(filterText)) {
        return true
      }
    }
    return false
  }

  override def toString: String = {
    if (oreDict) return "@" + filterText
    else return filterText
  }
}
