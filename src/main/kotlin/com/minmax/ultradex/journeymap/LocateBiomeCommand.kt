package com.minmax.ultradex.journeymap

import com.minmax.ultradex.UltraDex
import com.minmax.ultradex.util.BiomeUtils.getDisplayName
import com.mojang.brigadier.arguments.IntegerArgumentType
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
import net.minecraft.command.arguments.ResourceLocationArgument
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentUtils
import net.minecraft.util.text.TranslationTextComponent
import net.minecraftforge.registries.ForgeRegistries
import kotlin.math.floor
import kotlin.math.sqrt


object LocateBiomeCommand {
//    TODO(minmax): add suggestions
//    private val ALL_BIOMES: SuggestionProvider<CommandSource> = SuggestionProviders.register<CommandSource>(
//        ResourceLocation(UltraDex.MOD_ID, "all_biomes")
//    ) { _, builder -> ISuggestionProvider.suggestResource(ForgeRegistries.BIOMES.keys, builder) }

    val CMD: LiteralArgumentBuilder<CommandSource> = Commands.literal("biomes")
        .requires { cs -> cs.hasPermission(0) }
        .then(Commands
            .argument("biome", ResourceLocationArgument.id())
            .executes { ctx -> execute(ctx.source, ctx.getArgument("biome", ResourceLocation::class.java)) }
            .then(
                Commands.argument("maxDistance", IntegerArgumentType.integer())
                    .executes { ctx ->
                        execute(
                            ctx.source,
                            ctx.getArgument("biome", ResourceLocation::class.java),
                            IntegerArgumentType.getInteger(ctx, "maxDistance")
                        )
                    }
                    .then(
                        Commands.argument("step", IntegerArgumentType.integer())
                            .executes { ctx ->
                                execute(
                                    ctx.source,
                                    ctx.getArgument("biome", ResourceLocation::class.java),
                                    IntegerArgumentType.getInteger(ctx, "maxDistance"),
                                    IntegerArgumentType.getInteger(ctx, "step"),
                                )
                            })
            )
        )

    private fun execute(
        src: CommandSource,
        biomeRl: ResourceLocation,
        maxDistance: Int = MAX_DISTANCE,
        step: Int = STEPS
    ): Int {
        UltraDex.LOGGER.debug("locateBiome {} {} {}", biomeRl, maxDistance, step)
        val biome = ForgeRegistries.BIOMES.getValue(biomeRl) ?: throw INVALID_EXCEPTION.create(biomeRl)
        val biomeName = biome.getDisplayName()

        val biomePos = locateBiome(biomeRl, BlockPos(src.position), maxDistance, step)
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
            .flatMap { dx ->
                (-maxDistance..maxDistance step step).map { dz ->
                    BlockPos(pos.x + dx, pos.y, pos.z + dz)
                }
            }
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
        val biomeRl = BiomeHelper.getBiomeResource(biome)
        if (biomeRl != null) {
            UltraDex.LOGGER.debug("found biome {} at {}", biomeRl, newPos)
        }
        return biomeRl
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