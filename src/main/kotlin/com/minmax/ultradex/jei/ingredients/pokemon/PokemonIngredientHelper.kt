package com.minmax.ultradex.jei.ingredients.pokemon

import com.pixelmonmod.pixelmon.Pixelmon
import mezz.jei.api.ingredients.IIngredientHelper

@Suppress("OVERRIDE_DEPRECATION")
class PokemonIngredientHelper : IIngredientHelper<PokemonIngredient> {
    override fun getMatch(
        ingredients: MutableIterable<PokemonIngredient>,
        ingredientToMatch: PokemonIngredient
    ): PokemonIngredient? {
        for (pokemonIngredient in ingredients) {
            if (ingredientToMatch.getPokemon() == pokemonIngredient.getPokemon()) {
                return pokemonIngredient
            }
        }
        return null
    }

    override fun copyIngredient(ingredient: PokemonIngredient): PokemonIngredient = ingredient

    override fun getModId(ingredient: PokemonIngredient) = Pixelmon.MODID

    override fun getResourceId(ingredient: PokemonIngredient) = ingredient.getUniqueString()

    override fun getDisplayName(ingredient: PokemonIngredient) = ingredient.getUniqueString()

    override fun getUniqueId(ingredient: PokemonIngredient) = ingredient.getUniqueString()

    override fun getErrorInfo(ingredient: PokemonIngredient?) = ingredient?.getUniqueString() ?: "No error"
}