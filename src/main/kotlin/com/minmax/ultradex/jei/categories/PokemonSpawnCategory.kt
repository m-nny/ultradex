@file:Suppress("OVERRIDE_DEPRECATION")

package com.minmax.ultradex.jei.categories

import com.minmax.ultradex.config.Settings.JeiPositions
import com.minmax.ultradex.jei.PixelmonJEIPlugin
import com.minmax.ultradex.jei.ingredients.biome.BiomeIngredient
import com.minmax.ultradex.jei.ingredients.pokemon.PokemonIngredient
import com.minmax.ultradex.jei.recipes.PokemonSpawnRecipe
import com.minmax.ultradex.reference.Resources
import com.mojang.blaze3d.matrix.MatrixStack
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.util.ResourceLocation
import kotlin.math.min

class PokemonSpawnCategory(guiHelper: IGuiHelper) : IRecipeCategory<PokemonSpawnRecipe> {
    private val icon = guiHelper.createDrawable(Resources.Gui.Jei.TABS, 0, 0, 16, 16)
    private val background = guiHelper.createDrawable(Resources.Gui.Jei.POKEMON_SPAWNING, 0, 0, 168, 107)

    override fun getUid(): ResourceLocation = PixelmonJEIPlugin.POKEMON_SPAWNING

    override fun getRecipeClass() = PokemonSpawnRecipe::class.java

    override fun getTitle() = "Pokemon Spawn"

    override fun getBackground(): IDrawable = this.background

    override fun getIcon(): IDrawable = this.icon

    override fun setRecipe(recipeLayout: IRecipeLayout, recipe: PokemonSpawnRecipe, ingredients: IIngredients) {
        val biomeGroup = recipeLayout.getIngredientsGroup(BiomeIngredient.TYPE)

        var slot = 0
        var yOffset = 0
        for (i in 0 until JeiPositions.ITEMS_PER_ROW) {
            var xOffset = 0
            for (ii in 0 until JeiPositions.ITEMS_PER_COLUMN) {
                biomeGroup.init(slot++, true, JeiPositions.X_FIRST_ITEM + xOffset, JeiPositions.Y_FIRST_ITEM + yOffset)
                xOffset += JeiPositions.SLOT_SIZE
            }
            yOffset += JeiPositions.SLOT_SIZE
        }
        val slotSize = min(recipe.biomes.size, JeiPositions.ITEMS_TOTAL)
        for (i in 0 until slotSize) {
            biomeGroup[i] = recipe.biomes[i]
        }

        val pokemonGroup = recipeLayout.getIngredientsGroup(PokemonIngredient.TYPE)
        pokemonGroup.init(0, false, JeiPositions.X_POKEMON, JeiPositions.Y_POKEMON)
        pokemonGroup[0] = recipe.getPokemonIngredient()
    }

    override fun draw(recipe: PokemonSpawnRecipe, matrixStack: MatrixStack, mouseX: Double, mouseY: Double) {
        recipe.drawInfo(background.width, background.height, matrixStack, mouseX, mouseY)
    }

    override fun setIngredients(recipe: PokemonSpawnRecipe, ingredients: IIngredients) {
        recipe.setIngredients(ingredients)
    }
}