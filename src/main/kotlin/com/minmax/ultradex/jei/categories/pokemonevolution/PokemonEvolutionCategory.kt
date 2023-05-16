@file:Suppress("OVERRIDE_DEPRECATION")

package com.minmax.ultradex.jei.categories.pokemonevolution

import com.minmax.ultradex.jei.PixelmonJEIPlugin
import com.minmax.ultradex.jei.ingredients.pokemon.PokemonIngredient
import com.minmax.ultradex.reference.Resources
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.util.ResourceLocation

class PokemonEvolutionCategory(guiHelper: IGuiHelper) : IRecipeCategory<PokemonEvolutionRecipe> {
    private val icon = guiHelper.createDrawable(Resources.Gui.Jei.TABS, 0, 0, 16, 16)
    private val background = guiHelper.createDrawable(Resources.Gui.Jei.POKEMON_EVOLUTION, 29, 16, 116, 54)

    override fun getUid(): ResourceLocation = PixelmonJEIPlugin.POKEMON_EVOLUTIONS

    override fun getRecipeClass() = PokemonEvolutionRecipe::class.java

    override fun getTitle() = "Pokemon Evolution"

    override fun getBackground(): IDrawable = this.background

    override fun getIcon(): IDrawable = this.icon

    override fun setRecipe(recipeLayout: IRecipeLayout, recipe: PokemonEvolutionRecipe, ingredients: IIngredients) {
        val pokemonGroup = recipeLayout.getIngredientsGroup(PokemonIngredient.TYPE)
        pokemonGroup.init(0, true, PokemonIngredient.RENDERER, X_FROM_POKEMON, Y_FROM_POKEMON, 24, 24, 4, 4)
        pokemonGroup[0] = recipe.getFromPokemon()
        pokemonGroup.init(1, false, PokemonIngredient.RENDERER, X_TO_POKEMON, Y_TO_POKEMON, 24, 24, 4, 4)
        pokemonGroup[1] = recipe.getToPokemon()

        val needItem = recipe.getItems()
        recipeLayout.itemStacks.init(0, true, X_ITEM, Y_ITEM )
        recipeLayout.itemStacks[0] = needItem
    }

    override fun setIngredients(recipe: PokemonEvolutionRecipe, ingredients: IIngredients) =
        recipe.setIngredients(ingredients)

    companion object {
        private const val X_FROM_POKEMON = 14
        private const val Y_FROM_POKEMON = 15

        private const val X_TO_POKEMON = 81
        private const val Y_TO_POKEMON = 15

        private const val X_ITEM = 50
        private const val Y_ITEM = 0
    }
}