package com.minmax.ultradex.jei.registires

import com.minmax.ultradex.jei.ingredients.biome.BiomeIngredient
import com.minmax.ultradex.jei.ingredients.pokemon.PokemonIngredient
import com.minmax.ultradex.jei.recipes.PokemonDropRecipe
import com.minmax.ultradex.jei.recipes.PokemonEvolutionRecipe
import com.minmax.ultradex.jei.recipes.PokemonSpawnRecipe
import com.minmax.ultradex.jei.recipes.PokemonSpawnType
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnInfoPokemon
import com.pixelmonmod.pixelmon.entities.npcs.registry.DropItemRegistry
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning
import net.minecraftforge.registries.ForgeRegistries

object PokemonRegistries {
    fun getSpawnRecipes(): List<PokemonSpawnRecipe> {
        val spawnSets = PixelmonSpawning.getAll()
        return PokemonSpawnType.values().flatMap { type ->
            (spawnSets[type.type] ?: emptyList())
                .flatMap { it.spawnInfos }
                .filterIsInstance<SpawnInfoPokemon>()
                .filter { it.condition.biomes.isNotEmpty() }
                .map { PokemonSpawnRecipe(it, type) }
        }
    }

    fun getEvoRecipes(): List<PokemonEvolutionRecipe> =
        PixelmonSpecies.getAll()
            .flatMap { it.forms }
            .flatMap(PokemonEvolutionRecipe::fromForm)

    fun getDropsRecipes(): List<PokemonDropRecipe> =
        PixelmonSpecies.getAll()
            .flatMap { DropItemRegistry.pokemonDrops[it] ?: emptyList() }
            .map { PokemonDropRecipe(it) }

    fun getPokemons(): List<PokemonIngredient> =
        PixelmonSpecies.getAll()
            .flatMap { it.forms }
            .map(PokemonIngredient.Companion::fromForm)

    fun getBiomes(): List<BiomeIngredient> =
        ForgeRegistries.BIOMES.values.map { BiomeIngredient(it) }
}
