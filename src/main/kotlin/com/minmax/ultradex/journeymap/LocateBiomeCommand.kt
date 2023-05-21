package com.minmax.ultradex.journeymap

import com.minmax.ultradex.util.BiomeUtils.getDisplayName
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import journeymap.client.data.DataCache
import journeymap.client.io.FileHandler
import journeymap.client.model.MapType
import journeymap.client.model.RegionCoord
import journeymap.common.nbt.RegionDataStorageHandler
import net.minecraft.client.Minecraft
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.StringTextComponent
import net.minecraft.util.text.TextComponentUtils
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.biome.Biome
import net.minecraftforge.registries.ForgeRegistries


object LocateBiomeCommand {
//    var LOCATE_BIOME_COMMAND: LiteralArgumentBuilder<CommandSource> = Commands.literal("locatebiome")
//        .then(Commands.argument("biome", ResourceLocationArgument.id()))
//        .executes { ctx -> execute(ctx.source, ctx.getArgument("biome", ResourceLocation::class.java)) }


    private fun execute(src: CommandSource, biomeRl: ResourceLocation): Int {
        val biome = ForgeRegistries.BIOMES.getValue(biomeRl) ?: throw INVALID_EXCEPTION.create(biomeRl)
        val biomeName = biome.getDisplayName()

        val biomePos =
            locateBiome(biome, BlockPos(src.position), MAX_DISTANCE, STEPS)
                ?: throw NOT_FOUND_EXCEPTION.create(biomeName)
        src.sendSuccess(
            TranslationTextComponent(
                "commands.locate.success", biomeName,
                TextComponentUtils.wrapInSquareBrackets(
                    TranslationTextComponent("chat.coordinates", biomePos.x, "~", biomePos.z)
                )
            ), false
        )

        return 1
    }

    fun run(ctx: CommandContext<CommandSource>): Int {
        ctx.source.sendSuccess(StringTextComponent("Hello World!"), false)
        return 0
    }


    val CMD = Commands.literal("test")
        .requires { cs -> cs.hasPermission(0) }
        .executes { ctx -> run(ctx) }

//        private val CMD = LocateBiomeCommand()

    private fun locateBiome(targetBiome: Biome, pos: BlockPos, maxDistance: Int, step: Int): BlockPos? {
        val player = DataCache.getPlayer()
        val mapType = MapType.biome(player)
        for (dx in -maxDistance..maxDistance step step) {
            for (dz in -maxDistance..maxDistance step step) {
                val newPos = BlockPos(pos.x + dx, pos.y, pos.z + dz)
                val regionCoord = RegionCoord.fromChunkPos(
                    FileHandler.getJMWorldDir(Minecraft.getInstance()),
                    mapType,
                    newPos.x shr 4,
                    newPos.z shr 4
                )
                val regionData = RegionDataStorageHandler.getInstance()
                    .getRegionDataAsyncNoCache(regionCoord, mapType)
                val biome = regionData.getBiome(newPos)
                if (biome == targetBiome) {
                    return newPos
                }
            }
        }
        return null
    }

    private const val MAX_DISTANCE = 64000
    private const val STEPS = 8

    private val INVALID_EXCEPTION =
        DynamicCommandExceptionType { TranslationTextComponent("There is no biome named %s", it) }
    private val NOT_FOUND_EXCEPTION =
        DynamicCommandExceptionType {
            TranslationTextComponent(
                "Could not find a %s within reasonable distance",
                it
            )
        }
}