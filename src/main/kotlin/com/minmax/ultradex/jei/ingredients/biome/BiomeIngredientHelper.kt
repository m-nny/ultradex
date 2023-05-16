@file:Suppress("OVERRIDE_DEPRECATION")

package com.minmax.ultradex.jei.ingredients.biome

import com.minmax.ultradex.UltraDex
import mezz.jei.api.ingredients.IIngredientHelper

class BiomeIngredientHelper : IIngredientHelper<BiomeIngredient> {
    override fun getMatch(
        ingredients: MutableIterable<BiomeIngredient>,
        ingredientToMatch: BiomeIngredient
    ): BiomeIngredient? {
        for (ingredient in ingredients) {
            if (ingredientToMatch.getBiome() == ingredient.getBiome()) {
                return ingredient
            }
        }
        return null
    }

    override fun getDisplayName(ingredient: BiomeIngredient) = ingredient.getUniqueString()

    override fun getUniqueId(ingredient: BiomeIngredient) = ingredient.getUniqueString()

    override fun getModId(ingredient: BiomeIngredient) = ingredient.getResourceLocation()?.namespace ?: UltraDex.MOD_ID

    override fun getResourceId(ingredient: BiomeIngredient) = ingredient.getUniqueString()

    override fun copyIngredient(ingredient: BiomeIngredient) = ingredient

    override fun getErrorInfo(ingredient: BiomeIngredient?) = ingredient?.getUniqueString() ?: "No error"
}