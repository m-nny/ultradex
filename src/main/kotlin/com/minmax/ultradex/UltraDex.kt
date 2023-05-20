package com.minmax.ultradex

import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(UltraDex.MOD_ID)
object UltraDex {
    const val MOD_ID = "ultradex"

    val LOGGER: Logger = LogManager.getLogger()

    init {
        LOGGER.debug("UltraDex init")
    }
}