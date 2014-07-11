package com.awesomesauce.minecraft.forge.openautomation.api

import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary

class Filter(val filterString: String) {
  setFilter(filterString)
  var filterText: String = ""
  var oreDict: Boolean = false

  def setFilter(str: String) = {
    try {
      if (str.charAt(0) == '@') {
        oreDict = true
        filterText = str.substring(1)
      }
      else {
        filterText = str
      }
    }
    catch {
      case e: StringIndexOutOfBoundsException => filterText = str
    }
  }

  def doesFilterMatch(stack: ItemStack): Boolean = {
    if (oreDict) {
      val oreIds = OreDictionary.getOreIDs(stack)
      for (ore <- oreIds) {
        if (OreDictionary.getOreName(ore).contains(filterText)) {
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
