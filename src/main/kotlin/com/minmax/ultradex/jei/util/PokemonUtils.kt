package com.minmax.ultradex.jei.util

import com.pixelmonmod.api.pokemon.PokemonSpecification
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon
import com.pixelmonmod.pixelmon.enums.EnumGrowth

object PokemonUtils {
    fun getOrdinaryPokemon(pokemonSpec: PokemonSpecification): Pokemon {
        val pokemon = pokemonSpec.create()
        pokemon.growth = EnumGrowth.Ordinary
        return pokemon
    }
}