package com.minmax.ultradex.jei.util

import com.minmax.ultradex.jei.registires.PixelmonDropListener
import com.minmax.ultradex.jei.registires.PixelmonSpawnListener
import com.pixelmonmod.pixelmon.Pixelmon
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.JsonReloadListener
import net.minecraft.resources.IResourcePack
import net.minecraft.resources.ResourcePackType
import net.minecraft.resources.SimpleReloadableResourceManager
import net.minecraft.util.Unit
import net.minecraft.util.Util
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.packs.ModFileResourcePack
import java.util.concurrent.CompletableFuture

object ReloadUtil {
    fun reloadPixelmonRegistries() {
        val listeners = mutableListOf<JsonReloadListener>()

        PixelmonDropListener.clear()
        listeners.add(PixelmonDropListener())

        PixelmonSpawnListener.clear()
        listeners.add(PixelmonSpawnListener())

        val pack = getPixelmonPack()
        val serverResourceManger = SimpleReloadableResourceManager(ResourcePackType.SERVER_DATA)
        serverResourceManger.add(pack)
        listeners.forEach(serverResourceManger::registerReloadListener)
        val completableFuture = serverResourceManger.reload(
            Util.backgroundExecutor(),
            Minecraft.getInstance(),
            listOf(pack),
            CompletableFuture.completedFuture(Unit.INSTANCE)
        )
        Minecraft.getInstance().managedBlock(completableFuture::isDone)
    }

    private fun getPixelmonPack(): IResourcePack =
        ModFileResourcePack(ModList.get().getModFileById(Pixelmon.MODID).file)
}