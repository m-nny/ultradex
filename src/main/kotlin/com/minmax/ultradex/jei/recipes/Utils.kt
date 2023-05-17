package com.minmax.ultradex.jei.recipes

import com.pixelmonmod.pixelmon.api.pokemon.stats.evolution.Evolution
import com.pixelmonmod.pixelmon.api.pokemon.stats.evolution.conditions.EvoCondition
import com.pixelmonmod.pixelmon.api.pokemon.stats.evolution.conditions.HeldItemCondition
import com.pixelmonmod.pixelmon.api.pokemon.stats.evolution.types.InteractEvolution
import net.minecraft.item.ItemStack
import net.minecraft.util.text.IFormattableTextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.util.text.TranslationTextComponent

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

fun rarityString(rarity: Float): IFormattableTextComponent {
    val rarityKey = "pixelmon.command.wiki.spawning.rarity." + when {
        rarity <= 10.0 -> "ultra_rare"
        rarity <= 25.0 -> "very_rare"
        rarity <= 50.0 -> "rare"
        rarity <= 200.0 -> "uncommon"
        else -> "common"
    }

    return StringTextComponent("Rarity: ").append(TranslationTextComponent(rarityKey))
}
