package com.minmax.ultradex.jei.ingredients.pokemon

import com.mojang.blaze3d.matrix.MatrixStack
import com.pixelmonmod.pixelmon.client.gui.ScreenHelper
import mezz.jei.api.ingredients.IIngredientRenderer
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.util.text.ITextComponent

class PokemonIngredientRenderer : IIngredientRenderer<PokemonIngredient> {
    override fun render(matrixStack: MatrixStack, x: Int, y: Int, ingredient: PokemonIngredient?) {
        if (ingredient == null) return
        ScreenHelper.drawImageQuad(
            ingredient.getPokemon().sprite,
            matrixStack,
            x.toFloat(),
            y.toFloat(),
            16F,
            16F,
            0F,
            0F,
            1F,
            1F,
            1F,
            1F
        )
    }

    override fun getTooltip(ingredient: PokemonIngredient, tooltipFlag: ITooltipFlag): List<ITextComponent> {
        val name = ingredient.getStringTextComponent()
        return listOf(name)
    }
}