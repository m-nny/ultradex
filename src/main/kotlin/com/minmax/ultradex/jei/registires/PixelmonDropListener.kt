package com.minmax.ultradex.jei.registires

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.pixelmonmod.pixelmon.entities.npcs.registry.DropItemRegistry
import net.minecraft.client.resources.JsonReloadListener
import net.minecraft.profiler.IProfiler
import net.minecraft.resources.IResourceManager
import net.minecraft.util.ResourceLocation

class PixelmonDropListener : JsonReloadListener(Gson(), "drops") {
    override fun apply(
        resources: MutableMap<ResourceLocation, JsonElement>,
        manager: IResourceManager,
        profiler: IProfiler
    ) {
        resources.forEach(DropItemRegistry::registerDropItems)
    }

    companion object {
        fun clear() = DropItemRegistry.pokemonDrops.clear()
    }
}