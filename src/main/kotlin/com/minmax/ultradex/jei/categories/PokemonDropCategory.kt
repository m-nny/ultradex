package com.minmax.ultradex.jei.categories

import com.minmax.ultradex.config.Settings
import com.minmax.ultradex.jei.PixelmonJEIPlugin
import com.minmax.ultradex.jei.ingredients.pokemon.PokemonIngredient
import com.minmax.ultradex.jei.recipes.PokemonDropRecipe
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
    private val background = guiHelper.createDrawable(Resources.Gui.Jei.POKEMON_SPAWNING, 0, 0, 168, 107)

    override fun getUid() = PixelmonJEIPlugin.POKEMON_DROPS

    override fun getRecipeClass() = PokemonDropRecipe::class.java

    override fun getTitle() = "Pokemon Drops"

    override fun getBackground(): IDrawable = this.background

    override fun getIcon(): IDrawable = this.icon

    override fun setRecipe(recipeLayout: IRecipeLayout, recipe: PokemonDropRecipe, ingredients: IIngredients) {
        // drops
        var slot = 0
        var yOffset = 0
        for (i in 0 until Settings.JeiPositions.ITEMS_PER_ROW) {
            var xOffset = 0
            for (ii in 0 until Settings.JeiPositions.ITEMS_PER_COLUMN) {
                recipeLayout.itemStacks.init(
                    slot++,
                    false,
                    Settings.JeiPositions.X_FIRST_ITEM + xOffset,
                    Settings.JeiPositions.Y_FIRST_ITEM + yOffset
                )
                xOffset += Settings.JeiPositions.SLOT_SIZE
            }
            yOffset += Settings.JeiPositions.SLOT_SIZE
        }
        recipeLayout.itemStacks.addTooltipCallback(recipe)
        val drops = recipe.getDropItems()
        val slotIndex = min(drops.size, Settings.JeiPositions.ITEMS_TOTAL)
        for (i in 0 until slotIndex) {
            recipeLayout.itemStacks[i] = drops[i]
        }

        // pokemon
        val pokemonGroup = recipeLayout.getIngredientsGroup(PokemonIngredient.TYPE)
        pokemonGroup.init(0, true, Settings.JeiPositions.X_POKEMON, Settings.JeiPositions.Y_POKEMON)
        val pokemonIngredient = recipe.getPokemonIngredient()
        pokemonGroup[0] = pokemonIngredient
    }

    override fun draw(recipe: PokemonDropRecipe, matrixStack: MatrixStack, mouseX: Double, mouseY: Double) {
        recipe.drawInfo(background.width, background.height, matrixStack, mouseX, mouseY)
    }

    override fun setIngredients(recipe: PokemonDropRecipe, ingredients: IIngredients) {
        recipe.setIngredients(ingredients)
    }
}