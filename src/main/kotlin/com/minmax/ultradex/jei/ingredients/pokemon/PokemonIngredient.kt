package com.minmax.ultradex.jei.ingredients.pokemon

import com.minmax.ultradex.jei.util.PokemonUtils.getOrdinaryPokemon
import com.pixelmonmod.api.pokemon.PokemonSpecification
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory
import com.pixelmonmod.pixelmon.api.pokemon.species.Stats
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies
import mezz.jei.api.ingredients.IIngredientType
import net.minecraft.util.text.ITextComponent

class PokemonIngredient(private val pokemon: Pokemon) : IIngredientType<PokemonSpecification> {

    fun getPokemon() = this.pokemon

    override fun getIngredientClass() = PokemonSpecification::class.java

    fun getStringTextComponent(): ITextComponent =
        com.pixelmonmod.pixelmon.api.util.helpers.PokemonHelper.getFullName(pokemon)

    fun getUniqueString(): String = getStringTextComponent().string

    companion object {
        val TYPE = IIngredientType { PokemonIngredient::class.java }
        val HELPER = PokemonIngredientHelper()
        val RENDERER = PokemonIngredientRenderer()

        fun getIngredients(): List<PokemonIngredient> =
            PixelmonSpecies.getAll()
                .flatMap { species -> species.forms }
                .map(::fromForm)

        fun fromForm(form: Stats): PokemonIngredient {
            val pokemon = PokemonFactory.create(form.parentSpecies)
            pokemon.form = form
            return PokemonIngredient(pokemon)
        }

        fun fromSpec(spec: PokemonSpecification): PokemonIngredient = PokemonIngredient(getOrdinaryPokemon(spec))
    }
}