package com.minmax.ultradex.jei.util

import com.pixelmonmod.pixelmon.api.pokemon.drops.ItemWithChance
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent

object DropUtils {

    fun ItemWithChance.getTooltipText() =
        listOf<ITextComponent>(StringTextComponent(formatChance(this.chance) + " %"))

    private fun formatChance(chance: Double): String {
        val chance100 = chance * 100.0
        return if (chance100 < 10) {
            String.format("%.1f", chance100)
        } else {
            String.format("%2d", chance100.toInt())
        }
    }
}