package com.minmax.ultradex.jei.registires

import com.google.gson.JsonElement
import com.pixelmonmod.pixelmon.api.spawning.SpawnSet
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning
import net.minecraft.client.resources.JsonReloadListener
import net.minecraft.profiler.IProfiler
import net.minecraft.resources.IResourceManager
import net.minecraft.util.ResourceLocation

class PixelmonSpawnListener : JsonReloadListener(SpawnSet.GSON, "spawning") {
    override fun apply(
        resources: MutableMap<ResourceLocation, JsonElement>,
        manager: IResourceManager,
        profiler: IProfiler
    ) {
        for (entry in resources) {
            val set = SpawnSet.GSON.fromJson(entry.value, SpawnSet.CLASS)
            PixelmonSpawning.loadSpawnSet(set, entry.key)
        }
    }

    companion object {
        fun clear() = PixelmonSpawning.clearSpawning()
    }
}