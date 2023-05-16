package com.minmax.ultradex.jei.categories.pokemondrop

import com.minmax.ultradex.jei.ingredients.pokemon.PokemonIngredient
import com.minmax.ultradex.jei.util.DropUtils.getTooltipText
import com.minmax.ultradex.jei.util.Font
import com.minmax.ultradex.jei.util.PokemonUtils
import com.minmax.ultradex.jei.util.PokemonUtils.getPreviewOffsetY
import com.minmax.ultradex.jei.util.PokemonUtils.getPreviewScale
import com.minmax.ultradex.jei.util.RenderHelper
import com.mojang.blaze3d.matrix.MatrixStack
import com.pixelmonmod.pixelmon.api.pokemon.drops.PokemonDropInformation
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies
import com.pixelmonmod.pixelmon.api.util.helpers.PokemonHelper
import com.pixelmonmod.pixelmon.entities.npcs.registry.DropItemRegistry
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.ingredient.ITooltipCallback
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension
import net.minecraft.item.ItemStack
import net.minecraft.util.text.ITextComponent

class PokemonDropRecipe(private val pokemonDropInfo: PokemonDropInformation) : IRecipeCategoryExtension,
    ITooltipCallback<ItemStack> {
    private val pokemon = PokemonUtils.getOrdinaryPokemon(pokemonDropInfo.pokemonSpec)
    private val pixelmon = pokemon.getOrCreatePixelmon()
    private var scale = pixelmon.getPreviewScale()
    private var offsetY = pixelmon.getPreviewOffsetY()
    fun getDropItems(): List<ItemStack> =
        pokemonDropInfo.drops
            .map { obj -> obj.itemStack }
            .filter { itemStack -> !itemStack.isEmpty }


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
        RenderHelper.scissor(matrixStack, 7, 43, 59, 79)
        RenderHelper.renderEntity(
            matrixStack,
            37, 105 - offsetY, scale.toDouble(),
            38 - mouseX,
            70 - offsetY - mouseY,
            pixelmon
        )
        RenderHelper.stopScissor()
        Font.normal.printString(matrixStack, PokemonHelper.getFullName(pokemon).string, 7, 2)
        if (!pokemon.form.isDefault) {
            Font.normal.printString(matrixStack, "Form: " + pokemon.form.name, 7, 12)
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

    companion object {
        fun getRecipes(): List<PokemonDropRecipe> =
            PixelmonSpecies.getAll()
                .flatMap { species -> DropItemRegistry.pokemonDrops[species] ?: emptyList() }
                .map { pokemonDropInformation -> PokemonDropRecipe(pokemonDropInformation) }
    }
}