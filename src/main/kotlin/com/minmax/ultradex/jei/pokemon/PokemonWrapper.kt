package com.minmax.ultradex.jei.pokemon

import com.minmax.ultradex.config.Settings
import com.minmax.ultradex.pokemon.PokemonSpawnHelper.getBiomes
import com.mojang.blaze3d.matrix.MatrixStack
import com.pixelmonmod.pixelmon.api.pokemon.drops.ItemWithChance
import com.pixelmonmod.pixelmon.api.pokemon.drops.PokemonDropInformation
import com.pixelmonmod.pixelmon.api.util.helpers.PokemonHelper
import jeresources.util.Font
import jeresources.util.RenderHelper
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.ingredient.ITooltipCallback
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension
import net.minecraft.item.ItemStack
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import java.util.stream.Collectors
import javax.annotation.Nonnull

class PokemonWrapper(pokemonDropInformation: PokemonDropInformation) : IRecipeCategoryExtension,
    ITooltipCallback<ItemStack> {
    private val pokemon = PokemonUtils.createOrdinaryPokemon(pokemonDropInformation.pokemonSpec)
    private val livingEntity = pokemon.getOrCreatePixelmon();
    private var scale = PokemonUtils.getScale(livingEntity)
    private var offsetY = PokemonUtils.getOffsetY(livingEntity)
    private val biomes = getBiomes(pokemon)

    val drops: List<ItemWithChance> = pokemonDropInformation.drops

    override fun setIngredients(ingredients: IIngredients) {
        ingredients.setOutputs(VanillaTypes.ITEM,
            drops.stream()
                .map { obj -> obj.itemStack }
                .collect(Collectors.toList())
        )
    }

    override fun drawInfo(
        recipeWidth: Int,
        recipeHeight: Int,
        matrixStack: MatrixStack,
        mouseX: Double,
        mouseY: Double
    ) {
        RenderHelper.scissor(matrixStack, 7, 43, 59, 79)
        RenderHelper.renderEntity(
            matrixStack,
            37, 105 - offsetY, scale.toDouble(),
            38 - mouseX,
            70 - offsetY - mouseY,
            livingEntity
        )
        RenderHelper.stopScissor()
        var mobName = PokemonHelper.getFullName(pokemon).string
        if (Settings.showDevData) {
            val entityString = livingEntity.stringUUID
            mobName += " ($entityString)"
        }
        Font.normal.print(matrixStack, mobName, 7, 2)
        Font.normal.print(matrixStack, "Form: " + pokemon.form.name, 7, 12)
        Font.normal.print(matrixStack, "Biomes", 7, 22)
    }

    @Nonnull
    override fun getTooltipStrings(mouseX: Double, mouseY: Double): List<ITextComponent> {
        if (!isOnBiome(mouseX, mouseY)) {
            return emptyList()
        }
        return biomes.stream().map { biome -> StringTextComponent(biome) }.collect(Collectors.toList())
    }

    override fun onTooltip(
        slotIndex: Int,
        input: Boolean,
        ingredient: ItemStack,
        tooltip: MutableList<ITextComponent>
    ) {
        tooltip.addAll(getToolTip(ingredient))
    }

    private fun getToolTip(stack: ItemStack): List<ITextComponent> {
        for (item in drops) {
            if (stack.sameItem(item.item)) {
                return PokemonUtils.getTooltipText(item)
            }
        }
        return emptyList()
    }

    private fun isOnBiome(mouseX: Double, mouseY: Double): Boolean {
        return 2 <= mouseX && mouseX < 165 && 22 <= mouseY && mouseY < 22 + 10
    }
}
