package com.minmax.ultradex.jei.ingredients.biome

import com.minmax.ultradex.jei.util.RenderHelper
import com.mojang.blaze3d.matrix.MatrixStack
import mezz.jei.api.ingredients.IIngredientRenderer
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.util.text.ITextComponent

class BiomeIngredientRenderer : IIngredientRenderer<BiomeIngredient> {
    override fun render(matrixStack: MatrixStack, xPosition: Int, yPosition: Int, ingredient: BiomeIngredient?) {
        if (ingredient == null) return
        RenderHelper.renderItemStack(matrixStack, xPosition, yPosition, ingredient.getRepresentativeItemStack())
//        val itemRenderer = Minecraft.getInstance().itemRenderer
//        itemRenderer.renderGuiItem(ingredient.getRepresentativeItemStack(), xPosition, yPosition)
    }

    override fun getTooltip(ingredient: BiomeIngredient, tooltipFlag: ITooltipFlag): List<ITextComponent> {
        val name = ingredient.getStringTextComponent()
        return listOf(name)
    }
}