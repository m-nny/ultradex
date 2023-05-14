package com.minmax.ultradex.pokemon

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo
import com.pixelmonmod.pixelmon.api.spawning.SpawnSet
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnInfoPokemon
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning
import net.minecraft.util.ResourceLocation
import java.util.*
import java.util.stream.Collectors

object PokemonSpawnHelper {
    @JvmStatic
    fun getBiomes(pokemon: Pokemon): List<String> {
        val spawnSets = PixelmonSpawning.getAll()
        val standardSpawnSet = spawnSets["standard"] ?: return Collections.emptyList()

        return standardSpawnSet.stream()
            .flatMap { spawnSet: SpawnSet -> spawnSet.spawnInfos.stream() }
            .filter { spawnInfo: SpawnInfo? -> spawnInfo is SpawnInfoPokemon && spawnInfo.pokemonSpec.matches(pokemon) }
            .flatMap { spawnInfo: SpawnInfo -> spawnInfo.condition.biomes.stream() }
            .map { obj: ResourceLocation -> obj.toString() }
            .collect(Collectors.toList())
    }
}