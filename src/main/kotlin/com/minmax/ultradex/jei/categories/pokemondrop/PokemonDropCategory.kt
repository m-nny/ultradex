package com.minmax.ultradex.jei.categories.pokemondrop

import com.minmax.ultradex.config.Settings
import com.minmax.ultradex.jei.PixelmonJEIPlugin
import com.minmax.ultradex.jei.ingredients.pokemon.PokemonIngredient
import com.minmax.ultradex.reference.Resources
import com.mojang.blaze3d.matrix.MatrixStack
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.IRecipeCategory
import kotlin.math.min

@Suppress("OVERRIDE_DEPRECATION")
class PokemonDropCategory(guiHelper: IGuiHelper) : IRecipeCategory<PokemonDropRecipe> {
    private val icon = guiHelper.createDrawable(Resources.Gui.Jei.TABS, 0, 0, 16, 16)
    private val background = guiHelper.drawableBuilder(Resources.Gui.Jei.POKEMON_DROP, 0, 0, 163, 120)
        .addPadding(5, 5, 5, 5)
        .build()

    override fun getUid() = PixelmonJEIPlugin.POKEMON_DROPS

    override fun getRecipeClass() = PokemonDropRecipe::class.java

    override fun getTitle() = "Pokemon Drops"

    override fun getBackground(): IDrawable = this.background

    override fun getIcon(): IDrawable = this.icon

    override fun setRecipe(recipeLayout: IRecipeLayout, recipe: PokemonDropRecipe, ingredients: IIngredients) {
        // drops
        var xOffset = 0
        var slot = 0
        for (i in 0 until Settings.ITEMS_PER_ROW) {
            var yOffset = 0
            for (ii in 0 until Settings.ITEMS_PER_COLUMN) {
                recipeLayout.itemStacks.init(slot++, false, X_FIRST_ITEM + xOffset, Y_FIRST_ITEM + yOffset)
                yOffset += 80 / Settings.ITEMS_PER_COLUMN
            }
            xOffset += 72 / Settings.ITEMS_PER_ROW
        }
        recipeLayout.itemStacks.addTooltipCallback(recipe)
        val drops = recipe.getDropItems()
        val slotIndex = min(drops.size, Settings.ITEMS_PER_ROW * Settings.ITEMS_PER_COLUMN)
        for (i in 0 until slotIndex) {
            recipeLayout.itemStacks[i] = drops[i]
        }

        // pokemon
        val pokemonGroup = recipeLayout.getIngredientsGroup(PokemonIngredient.TYPE)
        pokemonGroup.init(0, true, X_POKEMON, Y_POKEMON)
        val pokemonIngredient = recipe.getPokemonIngredient()
        pokemonGroup[0] = pokemonIngredient
    }

    override fun draw(recipe: PokemonDropRecipe, matrixStack: MatrixStack, mouseX: Double, mouseY: Double) {
        recipe.drawInfo(background.width, background.height, matrixStack, mouseX, mouseY)
    }

    override fun setIngredients(recipe: PokemonDropRecipe, ingredients: IIngredients) {
        recipe.setIngredients(ingredients)
    }

    companion object {
        private const val X_FIRST_ITEM = 97
        private const val Y_FIRST_ITEM = 43

        private const val X_POKEMON = 151
        private const val Y_POKEMON = 22
    }
}