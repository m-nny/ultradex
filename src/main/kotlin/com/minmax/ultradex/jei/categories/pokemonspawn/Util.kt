package com.minmax.ultradex.jei.categories.pokemonspawn

import net.minecraft.util.text.IFormattableTextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.util.text.TranslationTextComponent

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