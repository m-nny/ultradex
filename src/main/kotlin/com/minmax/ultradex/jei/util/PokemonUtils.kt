package com.minmax.ultradex.jei.util

import com.pixelmonmod.api.pokemon.PokemonSpecification
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity
import com.pixelmonmod.pixelmon.enums.EnumGrowth

object PokemonUtils {
    fun PixelmonEntity.getPreviewScale(): Float {
        val width = this.bbWidth
        val height = this.bbHeight
        if (width <= height) {
            return when {
                height < 0.9 -> 50.0f
                height < 1 -> 35.0f
                height < 1.8 -> 33.0f
                height < 2 -> 32.0f
                height < 3 -> 24.0f
                height < 4 -> 20.0f
                else -> 10.0f
            }
        }
        return when {
            width < 1 -> 38.0f
            width < 2 -> 27.0f
            width < 3 -> 13.0f
            else -> 9.0f
        }
    }

    fun PixelmonEntity.getPreviewOffsetY(): Int {
        return when {
            this.isPokemon(PixelmonSpecies.TALONFLAME) -> -50
            else -> 0
        }
    }

    fun getOrdinaryPokemon(pokemonSpec: PokemonSpecification): Pokemon {
        val pokemon = pokemonSpec.create()
        pokemon.growth = EnumGrowth.Ordinary
        return pokemon
    }
}