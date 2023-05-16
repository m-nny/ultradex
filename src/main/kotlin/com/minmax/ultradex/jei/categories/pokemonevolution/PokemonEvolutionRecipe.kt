package com.minmax.ultradex.jei.categories.pokemonevolution

import com.minmax.ultradex.jei.ingredients.pokemon.PokemonIngredient
import com.minmax.ultradex.jei.util.PokemonUtils
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory
import com.pixelmonmod.pixelmon.api.pokemon.species.Stats
import com.pixelmonmod.pixelmon.api.pokemon.stats.evolution.Evolution
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension

class PokemonEvolutionRecipe(private val fromPokemon: Pokemon, private val evolution: Evolution) :
    IRecipeCategoryExtension {
    private val toPokemon = PokemonUtils.getOrdinaryPokemon(evolution.to)

    fun getFromPokemon() = PokemonIngredient(fromPokemon)

    fun getToPokemon() = PokemonIngredient(toPokemon)

    fun getItems() = evolution.getItems()

    override fun setIngredients(ingredients: IIngredients) {
        val needItem = getItems()
        if (needItem != null) {
            ingredients.setInput(VanillaTypes.ITEM, needItem)
        }
        ingredients.setInput(PokemonIngredient.TYPE, getFromPokemon())
        ingredients.setOutput(PokemonIngredient.TYPE, getToPokemon())
    }

    companion object {
        fun getRecipes(): List<PokemonEvolutionRecipe> =
            PixelmonSpecies.getAll()
                .flatMap { species -> species.forms }
                .flatMap(Companion::fromForm)

        private fun fromForm(form: Stats): List<PokemonEvolutionRecipe> {
            val pokemon = PokemonFactory.create(form.parentSpecies)
            pokemon.form = form
            return pokemon.getEvolutions(Evolution::class.java)
                .map { evolution -> PokemonEvolutionRecipe(pokemon, evolution) }
        }
    }
}