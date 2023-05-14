package com.minmax.ultradex

import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(UltraDex.MOD_ID)
object UltraDex {
    const val MOD_ID = "ultradex"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger()

    @JvmStatic
    fun rl(path: String): ResourceLocation {
        return ResourceLocation(MOD_ID, path)
    }
}