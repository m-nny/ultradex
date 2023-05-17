package com.minmax.ultradex.jei.recipes

import com.minmax.ultradex.config.Settings
import com.minmax.ultradex.jei.ingredients.pokemon.PokemonIngredient
import com.minmax.ultradex.jei.util.DropUtils.getTooltipText
import com.minmax.ultradex.jei.util.Font
import com.minmax.ultradex.jei.util.PokemonUtils
import com.mojang.blaze3d.matrix.MatrixStack
import com.pixelmonmod.pixelmon.api.pokemon.drops.PokemonDropInformation
import com.pixelmonmod.pixelmon.api.util.helpers.PokemonHelper
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.ingredient.ITooltipCallback
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension
import net.minecraft.item.ItemStack
import net.minecraft.util.text.ITextComponent

class PokemonDropRecipe(private val pokemonDropInfo: PokemonDropInformation) : IRecipeCategoryExtension,
    ITooltipCallback<ItemStack> {
    private val pokemon = PokemonUtils.getOrdinaryPokemon(pokemonDropInfo.pokemonSpec)
    fun getDropItems(): List<ItemStack> =
        pokemonDropInfo.drops
            .map { it.itemStack }
            .filter { !it.isEmpty }


    fun getPokemonIngredient() = PokemonIngredient(pokemon)

    override fun setIngredients(ingredients: IIngredients) {
        ingredients.setInput(PokemonIngredient.TYPE, getPokemonIngredient())
        ingredients.setOutputs(VanillaTypes.ITEM, getDropItems())
    }

    override fun drawInfo(
        recipeWidth: Int,
        recipeHeight: Int,
        matrixStack: MatrixStack,
        mouseX: Double,
        mouseY: Double
    ) {
        val xPos = Settings.JeiPositions.X_TEXT
        var yPos = Settings.JeiPositions.Y_TEXT
        Font.small.printString(matrixStack, PokemonHelper.getFullName(pokemon).string, xPos, yPos)
        yPos += 8
        if (!pokemon.form.isDefault) {
            Font.small.printString(matrixStack, "Form: " + pokemon.form.name, xPos, yPos)
        }
    }

    override fun onTooltip(
        slotIndex: Int,
        input: Boolean,
        stack: ItemStack,
        tooltip: MutableList<ITextComponent>
    ) {
        tooltip.addAll(getToolTip(stack))
    }

    private fun getToolTip(stack: ItemStack): List<ITextComponent> {
        for (itemWithChance in pokemonDropInfo.drops) {
            if (stack.sameItem(itemWithChance.item)) {
                return itemWithChance.getTooltipText()
            }
        }
        return emptyList()
    }
}