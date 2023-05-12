package com.minmax.ultradex.jei.PixelmonDrop;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.spawning.SpawnSet;
import com.pixelmonmod.pixelmon.api.spawning.archetypes.entities.pokemon.SpawnInfoPokemon;
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PokemonSpawnHelper {
    public static List<String> getBiomes(Pokemon pokemon) {
        Map<String, List<SpawnSet>> spawnSets = PixelmonSpawning.getAll();
        if (!spawnSets.containsKey("standard")) {
            return null;
        }

        return spawnSets.get("standard")
                .stream()
                .flatMap(spawnSet -> spawnSet.spawnInfos.stream())
                .filter(spawnInfo -> (spawnInfo instanceof SpawnInfoPokemon) && ((SpawnInfoPokemon) spawnInfo).getPokemonSpec().matches(pokemon))
                .flatMap(spawnInfo -> spawnInfo.condition.biomes.stream())
                .map(ResourceLocation::toString)
                .collect(Collectors.toList());
    }
}
