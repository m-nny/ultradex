package com.minmax.ultradex.journeymap

import com.minmax.ultradex.util.BiomeUtils.getDisplayName
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import journeymap.client.data.DataCache
import journeymap.client.io.FileHandler
import journeymap.client.model.MapType
import journeymap.client.model.RegionCoord
import journeymap.common.helper.BiomeHelper
import journeymap.common.nbt.RegionDataStorageHandler
import net.minecraft.client.Minecraft
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentUtils
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.biome.Biomes
import net.minecraftforge.registries.ForgeRegistries
import kotlin.math.floor
import kotlin.math.sqrt


object LocateBiomeCommand {
    val CMD: LiteralArgumentBuilder<CommandSource> = Commands.literal("test")
        .requires { cs -> cs.hasPermission(0) }
        .executes { ctx -> execute(ctx.source, Biomes.DESERT.location()) }

    private fun execute(src: CommandSource, biomeRl: ResourceLocation): Int {
        val biome = ForgeRegistries.BIOMES.getValue(biomeRl) ?: throw INVALID_EXCEPTION.create(biomeRl)
        val biomeName = biome.getDisplayName()

        val biomePos = locateBiome(biomeRl, BlockPos(src.position), MAX_DISTANCE, STEPS)
            ?: throw NOT_FOUND_EXCEPTION.create(biomeName)
        val distance = floor(BlockPos(src.position).distance(biomePos)).toInt()
        src.sendSuccess(
            TranslationTextComponent(
                "commands.locate.success",
                biomeName,
                TextComponentUtils.wrapInSquareBrackets(
                    TranslationTextComponent("chat.coordinates", biomePos.x, "~", biomePos.z)
                ),
                distance
            ), false
        )

        return distance
    }


    private fun locateBiome(targetBiome: ResourceLocation, pos: BlockPos, maxDistance: Int, step: Int): BlockPos? {
        val player = DataCache.getPlayer()
        val mapType = MapType.biome(player)
        return (-maxDistance..maxDistance step step)
            .zip(-maxDistance..maxDistance step step)
            .map { BlockPos(pos.x + it.first, pos.y, pos.z + it.second) }
            .find { getJMBiome(it, mapType) == targetBiome }
    }

    private fun getJMBiome(newPos: BlockPos, mapType: MapType): ResourceLocation? {
        val regionPos = RegionCoord.fromChunkPos(
            FileHandler.getJMWorldDir(Minecraft.getInstance()),
            mapType,
            newPos.x shr 4,
            newPos.z shr 4
        )
        val regionData = RegionDataStorageHandler.getInstance().getRegionDataAsyncNoCache(regionPos, mapType)
        val biome = regionData.getBiome(newPos) ?: return null
        return BiomeHelper.getBiomeResource(biome)
    }

    private fun BlockPos.distance(pos: BlockPos): Double {
        val dx = this.x - pos.x
        val dz = this.z - pos.z
        return sqrt((dx * dx + dz * dz).toDouble())
    }

    private const val MAX_DISTANCE = 128
    private const val STEPS = 64

    private val INVALID_EXCEPTION =
        DynamicCommandExceptionType { TranslationTextComponent("There is no biome named %s", it) }
    private val NOT_FOUND_EXCEPTION =
        DynamicCommandExceptionType { TranslationTextComponent("Could not find a %s within distance", it) }
}