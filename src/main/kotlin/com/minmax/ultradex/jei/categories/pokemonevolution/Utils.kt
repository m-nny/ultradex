package com.minmax.ultradex.jei.categories.pokemonevolution

import com.pixelmonmod.pixelmon.api.pokemon.stats.evolution.Evolution
import com.pixelmonmod.pixelmon.api.pokemon.stats.evolution.conditions.EvoCondition
import com.pixelmonmod.pixelmon.api.pokemon.stats.evolution.conditions.HeldItemCondition
import com.pixelmonmod.pixelmon.api.pokemon.stats.evolution.types.InteractEvolution
import net.minecraft.item.ItemStack

fun Evolution.getItems(): ItemStack? {
    if (this is InteractEvolution) {
        return this.item?.itemStack
    }
    if (this.conditions !== null) {
        for (condition in this.conditions) {
            val conditionItem = condition.getItems()
            if (conditionItem != null) {
                return conditionItem
            }
        }
    }
    return null
}

fun EvoCondition.getItems(): ItemStack? =
    when (this) {
        is HeldItemCondition -> this.item.itemStack
        else -> null
    }